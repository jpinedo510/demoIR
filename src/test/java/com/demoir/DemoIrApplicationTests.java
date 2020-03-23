package com.demoir;

import com.demoir.model.Cliente;
import com.demoir.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.validation.constraints.AssertTrue;
import java.util.Date;

@SpringBootTest
class DemoIrApplicationTests {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void creaCliente() {
        Cliente cliente = new Cliente();
        cliente.setApellido("Pinedo");
        cliente.setNombre("Camila");
        cliente.setFechaNacimiento(new Date());
        Cliente clienteSaved = clienteRepository.save(cliente);
        System.out.println("cliente registrado");
    }
}
