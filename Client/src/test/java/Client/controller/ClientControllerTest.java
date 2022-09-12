package Client.controller;

import Client.model.Client;
import Client.model.TypeClient;
import Client.repo.IClientRepo;
import Client.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ClientController.class)
@Import(ClientServiceImpl.class)
public class ClientControllerTest {

    @MockBean
    IClientRepo clientRepo;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void list() {
        Client client = new Client();
        client.setIdClient("63040dda45597124e84e0bd1");
        client.setName("First Register");
        client.setDocumentNumber("74589374");
        client.setDocumentType("DNI");
        client.setTypeClient(new TypeClient("natural",
                "prueba"));

        List<Client> list = new ArrayList<Client>();
        list.add(client);
        Flux<Client> clientFlux = Flux.fromIterable(list);

        Mockito.when(clientRepo.findByDocumentNumber("74589374"))
                .thenReturn(clientFlux.next());
        webTestClient
                .get()
                .uri("/api/client")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Client.class);
    }

    @Test
    void register() {
        Client client = new Client();
        client.setIdClient("63040dda45597124e84e0bd2");
        client.setName("Test register");
        client.setDocumentNumber("33289122");
        client.setDocumentType("DNI");
        client.setTypeClient(new TypeClient("natural",
                "prueba"));
        webTestClient
                .post()
                .uri("/api/client")
                .body(BodyInserters.fromObject(client))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void delete() {
        Mono<Client> voidReturn = Mono.empty();
        String id = "63040dda45597124e84e0bd1";
        Mockito
                .when(clientRepo.findById(id))
                .thenReturn(voidReturn)
                .thenReturn(null);

        webTestClient.delete()
                .uri("/api/client/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void clientbydocumentNumber() {
        Client client = new Client();
        client.setIdClient("63040dda45597124e84e0bd1");
        client.setName("First Register");
        client.setDocumentNumber("74589374");
        client.setDocumentType("DNI");
        client.setTypeClient(new TypeClient("natural",
                "prueba"));

        Mockito
                .when(clientRepo.findByDocumentNumber("74589374"))
                .thenReturn(Mono.just(client));

        webTestClient.get().uri("/api/client/documentNumber/{documentNumber}", 74589374)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isNotEmpty()
                //.jsonPath("$.id").isEqualTo("63040dda45597124e84e0bd1")
                .jsonPath("$.name").isEqualTo("First Register")
                .jsonPath("$.documentNumber").isEqualTo("74589374")
                .jsonPath("$.documentType").isEqualTo("DNI")
                .jsonPath("$.typeClient").isNotEmpty();

        Mockito.verify(clientRepo, times(1)).findByDocumentNumber("74589374");
    }

    @Test
    void update() {
        Client client = new Client();
        client.setIdClient("63040dda45597124e84e0bd2");
        client.setName("Test register");
        client.setDocumentNumber("33289122");
        client.setDocumentType("DNI");
        client.setTypeClient(new TypeClient("natural",
                "prueba"));
        webTestClient
                .put()
                .uri("/api/client")
                .body(BodyInserters.fromObject(client))
                .exchange()
                .expectStatus()
                .isOk();
    }
}