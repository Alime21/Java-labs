package msku.ceng.madlab.week7;
public interface BoardView {
    // '' tek tırnak char için; "" çift tırnak string için
    char PLAYER1_SYMBOL = 'X';
    char PLAYER2_SYMBOL = 'O';

    byte DRAW = 0;
    byte PLAYER_1_WINNER = 1;
    byte PLAYER_2_WINNER = 2;
    void newGame();

    void putSymbol(char symbol, byte row, byte col);
    void gameEnded(byte winner);
    void invalidPlay(byte row, byte col);

}
