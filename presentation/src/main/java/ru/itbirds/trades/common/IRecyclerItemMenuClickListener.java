package ru.itbirds.trades.common;

import ru.itbirds.data.model.Message;

public interface IRecyclerItemMenuClickListener {
    void editMessage(Message message);

    void deleteMessage(String documentId);
}
