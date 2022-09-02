package org.p2pchat.db;

import java.util.List;

import org.p2pchat.model.Message;
import org.p2pchat.model.Room;

public interface ChatRepository {

    void createRoom(Room room);

    void addMessages(int chatRoomId, List<Message> messages);

    List<Message> getMessages(int chatRoomId, long startTime, long endTime);

    Room getRoom(int id);

    int countLongPauses(int chatRoomId, long startTime, long endTime);

}