package model.builder;

import model.Client;

public class ClientBuilder {
    private Client client;

    public ClientBuilder() {
        client = new Client();
    }

    public ClientBuilder setName(String name) {
        client.setName(name);
        return this;
    }

    public ClientBuilder setAddress(String address) {
        client.setAddress(address);
        return this;
    }

    public ClientBuilder setIdCardNumber(String cardNumber) {
        client.setIdCardNumber(cardNumber);
        return this;
    }

    public ClientBuilder setPersonalNumericalCode(String numericalCode) {
        client.setPersonalNumericalCode(numericalCode);
        return this;
    }

    public ClientBuilder setId(Long id)
    {
        client.setId(id);
        return this;
    }

    public Client build() {
        return client;
    }
}