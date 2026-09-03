FROM node:24-alpine AS build

WORKDIR /app

RUN corepack enable \
    && corepack prepare pnpm@10.26.2 --activate

COPY package.json pnpm-lock.yaml ./

ENV CI true

RUN pnpm install --frozen-lockfile

COPY . .

RUN pnpm run build

FROM nginx:stable-alpine AS runtime

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist/guessimate-ui/browser /app

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
