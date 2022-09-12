package Client.service.impl;

import Client.model.Client;
import Client.model.TypeClient;
import Client.repo.IClientRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ClientServiceImplTest {

    @Mock
    IClientRepo clientRepo;

    @InjectMocks
    ClientServiceImpl clientService;

    @Autowired
    private Mono<Client> clientMono;
    @Autowired
    private Client clientClass;

    @BeforeEach
    void ini() {
        clientMono = Mono.just(new Client("63040dda45597124e84e0bd2",
                "Test register",
                "33289122",
                "DNI",
                new TypeClient("natural",
                        "prueba")));
        clientClass = new Client("63040dda45597124e84e0bd2",
                "Test register",
                "33289122",
                "DNI",
                new TypeClient("natural",
                        "prueba"));
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
        Mockito.when(clientRepo.save(client)).thenReturn(clientMono);
        Mono<Client> objClient = clientService.register(client);
        assertEquals(clientMono, objClient);
    }

    @Test
    void modify() {
        Client client = new Client();
        client.setIdClient("63040dda45597124e84e0bd2");
        client.setName("Test register");
        client.setDocumentNumber("33289122");
        client.setDocumentType("DNI");
        client.setTypeClient(new TypeClient("natural",
                "prueba"));
        Mockito.when(clientRepo.save(client)).thenReturn(clientMono);
        Mono<Client> objClient = clientService.modify(client);
        assertEquals(clientMono, objClient);
    }

    @Test
    void list() {
        List<Client> list = new ArrayList<Client>();
        list.add(clientClass);
        Flux<Client> clientFlux = Flux.fromIterable(list);
        Flux<Client> objClient = clientService.list();

        assertNotEquals(clientFlux, objClient);
    }

    @Test
    void listofId() {
        Mockito.when(clientRepo.findById("63040dda45597124e84e0bd1")).thenReturn(clientMono);
        Mono<Client> objClient = clientService.listofId("63040dda45597124e84e0bd1");
        assertEquals(clientMono, objClient);
    }

    @Test
    void delete() {
        boolean response = false;
        Mockito.when(clientRepo.findById("63040dda45597124e84e0bd1")).thenReturn(clientMono).thenReturn(null);
        final Mono<Client> byId = clientRepo.findById("63040dda45597124e84e0bd1");
        if (byId != null) {
            clientRepo.deleteById("63040dda45597124e84e0bd1");
            response = true;
        }
        //Mono<Client> objClient = clientService.delete("63040dda45597124e84e0bd1");
        Mockito.verify(clientRepo, times(1))
                .deleteById("63040dda45597124e84e0bd1");
        assertEquals(true,response);
    }

    @Test
    void clientbydocumentNumber() {
        Mockito.when(clientRepo.findByDocumentNumber("74589374")).thenReturn(clientMono);
        Mono<Client> objClient = clientService.clientbydocumentNumber("74589374");
        assertEquals(clientMono, objClient);
    }
}