package main.model;

import main.view.ObservadorPedidos;

public interface ObservablePedidos {
    public void agregarObservador(ObservadorPedidos o);
    public void eliminarObservador(ObservadorPedidos o);
    public void notificarObservadores(Pedido pedido);
}
