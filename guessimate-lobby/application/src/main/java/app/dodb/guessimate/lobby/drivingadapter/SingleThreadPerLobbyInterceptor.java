package app.dodb.guessimate.lobby.drivingadapter;

import app.dodb.guessimate.lobby.api.command.LobbyCommand;
import app.dodb.smd.api.command.Command;
import app.dodb.smd.api.command.CommandMessage;
import app.dodb.smd.api.command.bus.CommandBusInterceptor;
import app.dodb.smd.api.command.bus.CommandBusInterceptorChain;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static app.dodb.smd.api.utils.ExceptionUtils.rethrow;

@Component
class SingleThreadPerLobbyInterceptor implements CommandBusInterceptor, DisposableBean {

    private static final int PARTITIONS = Runtime.getRuntime().availableProcessors();

    private final List<ExecutorService> executors;

    SingleThreadPerLobbyInterceptor() {
        this.executors = IntStream.range(0, PARTITIONS)
            .mapToObj(index -> Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("lobby-" + index)
                .build()))
            .toList();
    }

    @Override
    public <R, C extends Command<R>> R intercept(CommandMessage<R, C> commandMessage, CommandBusInterceptorChain<R, C> chain) {
        if (commandMessage.payload() instanceof LobbyCommand lobbyCommand) {
            int index = (lobbyCommand.sessionId().hashCode() & Integer.MAX_VALUE) % PARTITIONS;
            ExecutorService executor = executors.get(index);

            try {
                return executor.submit(() -> chain.proceed(commandMessage)).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw rethrow(e);
            } catch (ExecutionException e) {
                throw rethrow(e.getCause());
            } catch (TimeoutException e) {
                throw rethrow(e);
            }
        }
        return chain.proceed(commandMessage);
    }

    @Override
    public void destroy() {
        executors.forEach(ExecutorService::shutdown);
    }
}