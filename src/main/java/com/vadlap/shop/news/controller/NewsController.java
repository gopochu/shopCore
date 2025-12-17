package com.vadlap.shop.news.controller;

import com.vadlap.shop.news.dto.NewsCreateRequest;
import com.vadlap.shop.news.dto.NewsResponse;
import com.vadlap.shop.news.service.NewsService;
import com.vadlap.shop.roles.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="News", description="Эндпоинты новостей")
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    // Рекомендуется использовать интерфейс INewsService, если он есть
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // --- Эндпоинт для создания новости ---

    @Operation(summary = "Создание новости")
    @PostMapping("/createNews")
    public ResponseEntity<?> createNews(
            // 1. Данные для создания новости из тела запроса (JSON)
            @RequestBody NewsCreateRequest createNewsRequest,

            // 2. Текущий аутентифицированный пользователь из Security Context
            // User должен быть вашим классом, реализующим UserDetails
            @AuthenticationPrincipal User currentUser,

            // 3. Место публикации (например, main_page или store_page) из параметров запроса
            @RequestParam("location") String newsLocation) {

        // Проверка, что пользователь аутентифицирован (хотя Spring Security
        // обычно делает это до контроллера, это хорошая защитная мера)
        if (currentUser == null) {
            return new ResponseEntity<>("Пользователь не аутентифицирован.", HttpStatus.UNAUTHORIZED);
        }

        try {
            // Вызов сервиса для создания новости
            NewsResponse response = newsService.createNews(createNewsRequest, currentUser, newsLocation);

            // Успешное создание
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // Обработка ошибок, связанных с бизнес-логикой (например, отсутствие прав)
            String errorMessage = e.getMessage();

            if (errorMessage != null && (errorMessage.contains("нет прав") || errorMessage.contains("Только"))) {
                // Если ошибка связана с авторизацией (ролью)
                return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN); // 403 Forbidden
            }

            // Общая ошибка сервера
            return new ResponseEntity<>("Ошибка при создании новости: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @Operation(summary = "Получение всех опубликованных новостей")
    @GetMapping("/published")
    public ResponseEntity<List<NewsResponse>> getAllPublishedNews() {
        return new ResponseEntity<>(newsService.findAllPublished(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    @Operation(summary = "Удаление новости магазина")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteNews(
            @PathVariable("id") Long newsId,
            @AuthenticationPrincipal User currentUser
    ) {
        if (currentUser == null) {
            return new ResponseEntity<>("Пользователь не аутентифицирован", HttpStatus.UNAUTHORIZED);
        }
        try {
            newsService.deleteNews(newsId, currentUser);
            // Возвращаем пустой ответ с кодом 204
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String errorMessage = e.getMessage();

            if (errorMessage != null) {
                if (errorMessage.contains("не найдена")) {
                    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
                }
                if (errorMessage.contains("нет прав") || errorMessage.contains("EDITOR может")) {
                    return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
                }
            }

            return new ResponseEntity<>("Ошибка при удалении новости: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}