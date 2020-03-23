package com.demoir.rest;

import com.demoir.model.Cliente;
import com.demoir.reponse.bean.ClienteResponse;
import com.demoir.reponse.bean.KpiClienteResponse;
import com.demoir.service.ClienteService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/clienteService")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/creacliente")
    public void creacliente(@RequestBody Cliente cliente) {
        clienteService.creacliente(cliente);
    }

    @GetMapping("/kpideclientes")
    public KpiClienteResponse kpideclientes() {
        return clienteService.kpideclientes();
    }

    @GetMapping("/listclientes")
    public List<ClienteResponse> listclientes() {
        return clienteService.listclientes();
    }
}
