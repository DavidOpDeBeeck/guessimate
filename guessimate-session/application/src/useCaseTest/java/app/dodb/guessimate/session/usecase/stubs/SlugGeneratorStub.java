package app.dodb.guessimate.session.usecase.stubs;

import app.dodb.guessimate.session.port.SlugGenerator;
import app.dodb.smd.spring.test.scope.annotation.SMDTestScope;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
@SMDTestScope
public class SlugGeneratorStub implements SlugGenerator {

    private final Deque<String> slugs = new LinkedList<>();

    public void stubSlug(String slug) {
        slugs.addLast(slug);
    }

    @Override
    public String generateSlug() {
        return slugs.removeFirst();
    }
}
