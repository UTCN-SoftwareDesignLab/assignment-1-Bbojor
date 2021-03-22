package service.client;

import model.Client;
import model.validation.ClientValidator;
import model.validation.Notification;
import repository.client.ClientRepository;

import java.util.List;

public class ClientServiceMySQL implements  ClientService{

    private final ClientRepository clientRepository;

    public ClientServiceMySQL(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Notification<Boolean> addClient(Client client) {
        Notification<Boolean> result = new Notification<>();

        ClientValidator clientValidator = new ClientValidator(client);
        boolean isClientValid = clientValidator.validate();
        if(!isClientValid)
        {
            clientValidator.getErrors().forEach(result::addError);
            return result;
        }

        result.setResult(clientRepository.addClient(client));

        return result;
    }

    @Override
    public Notification<Boolean> updateClient(Client client) {
        Notification<Boolean> result = new Notification<>();

        Notification<Client> oldClientNotification = clientRepository.getClientById(client.getId());
        if(oldClientNotification.hasErrors())
        {
            result.addError(oldClientNotification.getFormattedErrors());
            return result;
        }

        Client oldClient = oldClientNotification.getResult();

        if(client.getAddress() == null || client.getAddress().isBlank())
            client.setAddress(oldClient.getAddress());

        if(client.getIdCardNumber() == null || client.getIdCardNumber().isBlank())
            client.setIdCardNumber(oldClient.getIdCardNumber());

        if(client.getName() == null || client.getName().isBlank())
            client.setName(oldClient.getName());

        if(client.getPersonalNumericalCode() == null || client.getPersonalNumericalCode().isBlank())
            client.setPersonalNumericalCode(oldClient.getPersonalNumericalCode());

        ClientValidator clientValidator = new ClientValidator(client);
        boolean isClientValid = clientValidator.validate();
        if(!isClientValid)
        {
            clientValidator.getErrors().forEach(result::addError);
            return result;
        }

        result.setResult(clientRepository.updateClient(client));
        return result;
    }

    @Override
    public Notification<Boolean> deleteClient(Client client) {
        Notification<Boolean> result = new Notification<>();
        result.setResult(clientRepository.deleteClient(client));
        return result;
    }

    @Override
    public void deleteAll() {
        clientRepository.deleteAll();
    }
}
