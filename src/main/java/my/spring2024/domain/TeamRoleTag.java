package my.spring2024.domain;

/**
 * Перечеслитель для ролей в команде
 */
public enum TeamRoleTag {
    /**
     * Роль тимлида
     */
    TEAMLEAD,
    /**
     * Роль разработчика
     */
    DEVELOPER,
    /**
     * Роль дизайнера
     */
    DESIGNER,
    /**
     * Роль аналитика
     */
    ANALYST,
    /**
     * Роль Геймдизанера
     */
    GAMEDESIGNER,
    /**
     * Роль фронтенд разработчика
     */
    FRONTEND,
    /**
     * Роль бэкенд разработчика
     */
    BACKEND,
    /**
     * Роль фуллстак разработчика
     */
    FULLSTACK,
    /**
     * Другая роль
     */
    OTHER
}