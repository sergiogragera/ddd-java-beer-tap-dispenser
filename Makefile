.PHONY: help
help:
	@echo "make help                  Show this help message"
	@echo "make lint                  Run the code linter(s) and print any warnings"
	@echo "make format                Correctly format the code"
	@echo "make test                  Run the unit tests"
	@echo "make integration-test      Run the integration tests"
	@echo "make sure                  Make sure that the formatter, linter, tests, etc all pass"
	@echo "make docker                Make the app's Docker image"
	@echo "make run-docker            Run the app's Docker image locally. "
	@echo "                           This command exists for conveniently testing "
	@echo "                           the Docker image locally in production mode. "

.PHONY: lint
lint:
	@./mvnw validate

.PHONY: format
format:
	@./mvnw process-sources

.PHONY: integration-test
integration-test:
	@./mvnw -Pintegration-test test

.PHONY: test
test:
	@./mvnw -Punit-test test

.PHONY: sure
sure:
	@./mvnw verify -B

.PHONY: docker
docker:
	@git archive --format=tar.gz HEAD | docker build -t rviewer/beer-tap-dispenser:$(DOCKER_TAG) -

.PHONY: run-docker
run-docker:
	@docker run \
		--rm \
		-p 8080:8080 \
		--name beer-tap-dispenser \
		rviewer/beer-tap-dispenser:$(DOCKER_TAG)

DOCKER_TAG = dev
