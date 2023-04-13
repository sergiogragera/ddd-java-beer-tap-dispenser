.PHONY: down up test

down:
	docker compose down

up:
	docker compose run -d -p "8080:8080" java-skeleton-api ./mvnw clean spring-boot:run -Dtests.skip=true

test:
	docker compose run --rm --no-deps -p "8080:8080" java-skeleton-api ./mvnw verify

coverage: test