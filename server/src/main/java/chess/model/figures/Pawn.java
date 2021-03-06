package chess.model.figures;

import chess.Constants;
import chess.exceptions.ReplacePawnException;
import chess.model.Cell;
import chess.model.Figure;
import chess.model.Game;
import chess.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>Pawn</code> figure.
 */
public class Pawn extends Figure {

    public Pawn(Type type, Cell cell) {
        super(type, cell);
    }
    public Pawn() {
    }
    @Override
    public void move(Cell destination) throws ReplacePawnException {
        super.move(destination);
        if ((destination.getY() == Constants.BOARDSIZE-1 && getType() == Type.BLACK) || (destination.getY() == 0 && getType() == Type.WHITE)) {
            throw new ReplacePawnException();
        }
    }

    public List<Cell> allAccessibleMove() {
        List<Cell> validCells = new ArrayList<Cell>();

        validCells.clear();
        if (getCell() == null) return validCells;
        Game game = getCell().getParentGame();
        int koef = 1;
        if (getType() == Type.WHITE) {
            koef = -1;
        }

        if (getCell().getY() + 1 * koef >= 0 && getCell().getY() + 1 * koef < Constants.BOARDSIZE) {
            if (!game.getCell(getCell().getX(), getCell().getY() + 1 * koef).isFigure()) {
                validCells.add(game.getCell(getCell().getX(), getCell().getY() + 1 * koef));
            }
        }
        if (isFirstMove()) {
            if (getCell().getY() + 2 * koef >= 0 && getCell().getY() + 2 * koef < Constants.BOARDSIZE) {
                if (!game.getCell(getCell().getX(), getCell().getY() + 2 * koef).isFigure() && !game.getCell(getCell().getX(), getCell().getY() + 1 * koef).isFigure()) {
                    validCells.add(game.getCell(getCell().getX(), getCell().getY() + 2 * koef));
                }
            }
        }
        if (getCell().getX() + 1 * koef >= 0 && getCell().getX() + 1 * koef < Constants.BOARDSIZE && getCell().getY() + 1 * koef >= 0 && getCell().getY() + 1 * koef < Constants.BOARDSIZE) {
            if (!getCell().isFriendlyCell(game.getCell(getCell().getX() + 1 * koef, getCell().getY() + 1 * koef).getFigure()) && game.getCell(getCell().getX() + 1 * koef, getCell().getY() + 1 * koef).getFigure() != null) {
                validCells.add(game.getCell(getCell().getX() + 1 * koef, getCell().getY() + 1 * koef));
            }
        }
        if (getCell().getX() - 1 * koef >= 0 && getCell().getX() - 1 * koef < Constants.BOARDSIZE && getCell().getY() + 1 * koef >= 0 && getCell().getY() + 1 * koef < Constants.BOARDSIZE) {
            if (!getCell().isFriendlyCell(game.getCell(getCell().getX() - 1 * koef, getCell().getY() + 1 * koef).getFigure()) && game.getCell(getCell().getX() - 1 * koef, getCell().getY() + 1 * koef).getFigure() != null) {
                validCells.add(game.getCell(getCell().getX() - 1 * koef, getCell().getY() + 1 * koef));
            }
        }
        return validCells;
    }
}
