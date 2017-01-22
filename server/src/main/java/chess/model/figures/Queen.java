package chess.model.figures;

import chess.model.Cell;
import chess.model.Figure;
import chess.model.Game;
import chess.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17.01.2017.
 */
public class Queen extends Figure {
    public Queen(Type type) {
        super(type);
    }

    public Queen(Type type, Cell cell) {
        super(type, cell);
    }

    public void checkPath(List<Cell> validCells, int x, int y, int stepX, int stepY) {
        Game game = getCell().getParentGame();
        int i = x + stepX;
        int j = y + stepY;
        while (i < 8 && j < 8 && i >= 0 && j >= 0) {
            if (game.getCell(i, j) != null) {
                if (!getCell().isFriendlyCell(game.getCell(i, j).getFigure())) {
                    validCells.add(game.getCell(i, j));
                    if (game.getCell(i, j).isFigure()) {
                        System.out.println(game.getCell(i, j).getFigure().getClass().getName());
                        break;
                    }
                }
            }
            i += stepX;
            j += stepY;
        }
    }

    public List<Cell> allAccessibleMove() {
        List<Cell> validCells = new ArrayList<Cell>();
        Game game = getCell().getParentGame();
        checkPath(validCells, getCell().getX(), getCell().getY(), 1, 0);
        checkPath(validCells, getCell().getX(), getCell().getY(), -1, 0);
        checkPath(validCells, getCell().getX(), getCell().getY(), 0, 1);
        checkPath(validCells, getCell().getX(), getCell().getY(), 0, -1);
        checkPath(validCells, getCell().getX(), getCell().getY(), 1, 1);
        checkPath(validCells, getCell().getX(), getCell().getY(), 1, -1);
        checkPath(validCells, getCell().getX(), getCell().getY(), -1, 1);
        checkPath(validCells, getCell().getX(), getCell().getY(), -1, -1);
        return validCells;
    }
}
