package com.demoir.service;

import com.demoir.model.Cliente;
import com.demoir.reponse.bean.ClienteResponse;
import com.demoir.reponse.bean.KpiClienteResponse;
import com.demoir.repository.ClienteRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    /*** SERVICIO "creacliente" ***/
    public void creacliente(Cliente cliente) {
        cliente.setId(clienteRepository.findAll().size() + 1);
        clienteRepository.save(cliente);
    }

    /*** SERVICIO "kpiclientes" ***/
    public KpiClienteResponse kpideclientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        KpiClienteResponse kpiClienteResponse = new KpiClienteResponse();
        kpiClienteResponse.setPromedioEdad(getPromedioEdadesClientes(clientes));
        kpiClienteResponse.setDesviacionEstandar(getDesviacionEstandar(clientes));
        return kpiClienteResponse;
    }

    private double getPromedioEdadesClientes(List<Cliente> clientes) {
        List<Integer> edadesClientes = getEdadesClientes(clientes);
        if (edadesClientes == null) {
            return 0.0;
        }

        int sumaEdades = 0;
        for (Integer edad : edadesClientes) {
            sumaEdades += edad;
        }

        return BigDecimal.valueOf(sumaEdades / edadesClientes.size())
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    private int getEdadCliente(Date fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }

        Calendar fechaInicio = getCalendar(fechaNacimiento);
        Calendar fechaActual = getCalendar(new Date());
        return fechaActual.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);
    }

    private Calendar getCalendar(Date fecha) {
        if (fecha == null) {
            fecha = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        return calendar;
    }

    private double getDesviacionEstandar(List<Cliente> clientes) {
        //FORMULA: DE = RAIZ CUADRADA -->  (SUM|x - u|2)/N
        //DONDE x: el conjunto de datos (edad de cada cliente); u: la media del conjuntos de datos; N: cantidad de datos
        List<Integer> x = getEdadesClientes(clientes);
        if (x == null) {
            return 0.0;
        }

        int n = x.size();
        double u = getPromedioEdadesClientes(clientes);
        double sumatoriadeXmenosUelevadoal2 = sumatoriadeXmenosUelevadoal2(x, u);
        return BigDecimal.valueOf(Math.sqrt(sumatoriadeXmenosUelevadoal2 / n))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    private List<Integer> getEdadesClientes(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }

        List<Integer> edadesClientes = new ArrayList<>();
        clientes.forEach(cliente -> {
            int edadCliente = getEdadCliente(cliente.getFechaNacimiento());
            edadesClientes.add(edadCliente);
        });

        return edadesClientes;
    }

    private double sumatoriadeXmenosUelevadoal2(List<Integer> x, double u) {
        double sumatoria = 0.0;
        for (Integer dato : x) {
            sumatoria += Math.pow(Math.abs(dato - u), 2);
        }
        return sumatoria;
    }

    /*** SERVICIO "listclientes" ***/
    public List<ClienteResponse> listclientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteResponse> clienteResponses = new ArrayList<>();
        clientes.forEach(cliente -> {
            ClienteResponse clienteResponse = new ClienteResponse();
            clienteResponse.setNombre(cliente.getNombre());
            clienteResponse.setApellido(cliente.getApellido());
            clienteResponse.setFechaNacimiento(df.format(cliente.getFechaNacimiento()));
            clienteResponse.setFechaMuerte("Fecha probable de muerte en " +
                    getFechaProbableMuerte(cliente.getFechaNacimiento()) + " años.");

            clienteResponses.add(clienteResponse);
        });

        return clienteResponses;
    }

    private int getFechaProbableMuerte(Date fechaNacimiento) {
        int edadMaxima = 65;    //ASUMIENDO EDAD MÁXIMA DE VIDA: 65 AÑOS
        int edadActual = getEdadCliente(fechaNacimiento);
        if (edadActual >= edadMaxima) {
            return 0;
        }
        return edadMaxima - edadActual;
    }
}
