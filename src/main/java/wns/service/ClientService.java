package wns.service;


import org.springframework.stereotype.Service;
import wns.entity.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    public static Map<Long,Client> db = new HashMap<>();

    public Client findClientFromDB(long chatId)
    {
        return db.get(chatId);
    }

    public void saveClientToDB(Client client)
    {
        db.put(client.getChatId(),client);
    }

    public List<Client> findAllFromDB()
    {
        Collection<Client> values = db.values();
        return (List<Client>) values;
    }

}
