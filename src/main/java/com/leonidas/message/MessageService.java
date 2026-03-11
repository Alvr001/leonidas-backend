package com.leonidas.message;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonidas.group.Group;
import com.leonidas.group.GroupRepository;
import com.leonidas.message.dto.SendMessageDto;
import com.leonidas.user.User;
import com.leonidas.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public Message sendMessage(SendMessageDto dto) {

        User sender = userRepository.findById(dto.senderId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Group group = groupRepository.findById(dto.groupId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        // Verificar membresía sin cargar lazy collection
        boolean esMiembro = groupRepository.existsMember(dto.groupId(), dto.senderId());

        if (!esMiembro) {
            throw new RuntimeException("El usuario no es miembro de este grupo");
        }

        Message message = new Message();
        message.setContent(dto.content());
        message.setType(dto.type());
        message.setSender(sender);
        message.setGroup(group);

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByGroup(Long groupId) {
        return messageRepository.findByGroupIdOrderByCreatedAtAsc(groupId);
    }

    public List<Message> getAlertsByGroup(Long groupId) {
        return messageRepository.findByGroupIdAndType(groupId, Message.MessageType.ALERT);
    }
}