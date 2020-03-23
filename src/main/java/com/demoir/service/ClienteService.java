package com.demoir.service;

import com.demoir.model.Cliente;
import com.demoir.model.KpiClienteResponse;
import com.demoir.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

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
        return Math.sqrt(sumatoriadeXmenosUelevadoal2 / n);
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
    public List<Cliente> listclientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        clientes.forEach(cliente -> {
            cliente.setFechaMuerte("Fecha probable de muerte en " +
                    getFechaProbableMuerte(cliente.getFechaNacimiento()) + " años.");
        });
        return clientes;
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
