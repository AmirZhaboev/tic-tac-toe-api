// package tictactoe.di;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import tictactoe.datasource.mapper.GameMapper;
// import tictactoe.datasource.repository.GameRepository;

// import tictactoe.domain.service.*;
// import tictactoe.web.mapper.GameWebMapper;

// @Configuration
// public class AppConfig {

// @Bean
// public GameMapper gameMapper() {
// return new GameMapper();
// }

// @Bean
// public GameFieldService gameFieldService() {
// return new GameFieldServiceImpl();
// }

// @Bean
// public GameLogicService gameLogicService() {
// return new GameLogicServiceImpl();
// }

// @Bean
// public GameService gameService(GameRepository gameRepository, GameMapper
// mapper, GameFieldService fieldService,
// GameLogicService gameLogicService) {
// return new GameServiceImpl(gameRepository, mapper, fieldService,
// gameLogicService);
// }

// @Bean
// public GameWebMapper gameWebMapper() {
// return new GameWebMapper();
// }
// }
