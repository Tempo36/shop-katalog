package com.simplemessenger.controller;

import com.simplemessenger.model.User;
import com.simplemessenger.model.Message;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessengerController {
    
    // Хранилище пользователей (телефон -> User)
    private Map<String, User> users = new ConcurrentHashMap<>();
    
    // Хранилище сообщений (ключ: "отТелефон_кТелефон", значение: список сообщений)
    private Map<String, List<Message>> messages = new ConcurrentHashMap<>();
    
    // Текущие сессии (телефон -> WebSocket сессия)
    private Map<String, String> sessions = new ConcurrentHashMap<>();
    
    // Регистрация пользователя
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String name = request.get("name");
        
        Map<String, Object> response = new HashMap<>();
        
        if (phone == null || phone.trim().isEmpty() || 
            name == null || name.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Телефон и имя обязательны");
            return response;
        }
        
        if (users.containsKey(phone)) {
            response.put("success", false);
            response.put("message", "Пользователь с таким телефоном уже существует");
            return response;
        }
        
        User user = new User(phone, name);
        users.put(phone, user);
        
        response.put("success", true);
        response.put("message", "Регистрация успешна");
        response.put("user", user);
        
        return response;
    }
    
    // Поиск пользователей по имени
    @GetMapping("/search")
    public Map<String, Object> searchUsers(@RequestParam String query) {
        List<User> foundUsers = users.values().stream()
            .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", foundUsers);
        
        return response;
    }
    
    // Получение списка всех пользователей
    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", new ArrayList<>(users.values()));
        return response;
    }
    
    // Отправка сообщения
    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestBody Message message) {
        Map<String, Object> response = new HashMap<>();
        
        if (!users.containsKey(message.getFromPhone()) || 
            !users.containsKey(message.getToPhone())) {
            response.put("success", false);
            response.put("message", "Пользователь не найден");
            return response;
        }
        
        String key = getConversationKey(message.getFromPhone(), message.getToPhone());
        messages.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
        
        response.put("success", true);
        response.put("message", "Сообщение отправлено");
        
        return response;
    }
    
    // Получение истории сообщений
    @GetMapping("/messages")
    public Map<String, Object> getMessages(@RequestParam String user1, 
                                          @RequestParam String user2) {
        String key1 = getConversationKey(user1, user2);
        String key2 = getConversationKey(user2, user1);
        
        List<Message> conversation = new ArrayList<>();
        
        if (messages.containsKey(key1)) {
            conversation.addAll(messages.get(key1));
        }
        
        if (messages.containsKey(key2)) {
            conversation.addAll(messages.get(key2));
        }
        
        // Сортировка по времени
        conversation.sort(Comparator.comparing(Message::getTimestamp));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messages", conversation);
        
        return response;
    }
    
    // Установка статуса онлайн
    @PostMapping("/online")
    public Map<String, Object> setOnline(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        boolean online = Boolean.parseBoolean(request.get("online"));
        
        Map<String, Object> response = new HashMap<>();
        
        if (users.containsKey(phone)) {
            users.get(phone).setOnline(online);
            response.put("success", true);
        } else {
            response.put("success", false);
        }
        
        return response;
    }
    
    private String getConversationKey(String phone1, String phone2) {
        return phone1 + "_" + phone2;
    }
}