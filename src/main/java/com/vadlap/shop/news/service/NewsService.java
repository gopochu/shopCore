package com.vadlap.shop.news.service;

import com.vadlap.shop.news.NewsMapper;
import com.vadlap.shop.news.dto.NewsResponse;
import com.vadlap.shop.news.dto.interfaces.ICreateNews;
import com.vadlap.shop.news.entity.NewsEntity;
import com.vadlap.shop.news.repository.INewsRepository;
import com.vadlap.shop.roles.Role;
import com.vadlap.shop.roles.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService implements INewsService {

    private final INewsRepository newsRepository; // Добавляем репозиторий для сохранения

    public NewsService(INewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Создает новость с учетом роли пользователя и места публикации.
     * @param createNewsDto DTO с данными новости.
     * @param currentUser Текущий аутентифицированный пользователь.
     * @param newsLocation Место публикации (например, "main_page" или "store").
     * @return DTO ответа.
     * @throws Exception Если у пользователя нет прав для публикации в указанном месте.
     */
    @Override
    public NewsResponse createNews(ICreateNews createNewsDto, User currentUser, String newsLocation) throws Exception {

        Role role = currentUser.getRole();

        if (role != Role.ADMIN && role != Role.EDITOR) {
            throw new Exception("У пользователя нет прав для создания новости.");
        }

        boolean isMainPage = "main_page".equalsIgnoreCase(newsLocation);
        boolean isStorePage = "store_page".equalsIgnoreCase(newsLocation); // Предполагаем такое название для страницы магазина

        if (isMainPage && role != Role.ADMIN) {
            throw new Exception("Только ADMIN может публиковать новости на главной странице.");
        }

        if (isStorePage && role != Role.EDITOR) {
            throw new Exception("Только EDITOR может публиковать новости на странице своего магазина.");
        }

        // *ВНИМАНИЕ*: В реальном приложении для store_page нужно также проверить,
        // принадлежит ли новость именно этому магазину/автору.
        // Здесь мы просто проверяем роль.

        // 3. Создание сущности и сохранение
        NewsEntity newsEntity = NewsMapper.createNewsToNewsEntity(createNewsDto, currentUser.getId().toString());

        // Добавьте поле для места публикации в NewsEntity,
        // чтобы можно было фильтровать новости по месту (main_page/store_page)
        // newsEntity.setLocation(newsLocation);

        NewsEntity savedEntity = newsRepository.save(newsEntity);

        return NewsMapper.newsEntityToNewsResponse(savedEntity);
    }

    @Override
    public List<NewsResponse> findAllPublished() {
        return List.of();
    }

    @Override
    public void deleteNews(Long newsId, User currentUser) throws Exception {
        Role role = currentUser.getRole();

        if(role != Role.EDITOR && role!= Role.ADMIN) {
            throw new Exception("У вас нет прав на удаление новости");
        }

        Optional<NewsEntity> newsOptional = newsRepository.findById(newsId);

        if(newsOptional.isEmpty()) {
            throw new Exception("Роль не существует");
        }

        NewsEntity newsToDelete = newsOptional.get();

        if(role == Role.EDITOR) {
            if(!newsToDelete.getAuthor().equals(currentUser.getId())) {
                throw new Exception("У вас нет прав на удаление новости");
            } else {
                newsRepository.delete(newsToDelete);
            }
        }
    }
}

