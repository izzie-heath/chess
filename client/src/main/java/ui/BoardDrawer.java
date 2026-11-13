package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    public static void draw(ChessBoard board, ChessGame.TeamColor perspective){
        if(perspective == ChessGame.TeamColor.WHITE){
            String columns = "abcdefgh";
            String paddedColumns = "  ";
            for(int s=0; s<columns.length(); s++){
                paddedColumns += " " + columns.charAt(s) + " ";
            }
            System.out.println(paddedColumns);
            for(int r=8; r>=1; r--){
                System.out.print(RESET_TEXT_COLOR);
                System.out.print(r + " ");
                for(int c=1; c<=8; c++){
                    buildBoard(board, r, c);
                }
                System.out.print(RESET_BG_COLOR);
                System.out.print(RESET_TEXT_COLOR);
                System.out.print(" " + r);
                System.out.println();
            }
            System.out.println(paddedColumns);
        } else{
            String columns = "hgfedcba";
            String paddedColumns = "  ";
            for(int s=0; s<columns.length(); s++){
                paddedColumns += " " + columns.charAt(s) + " ";
            }
            System.out.println(paddedColumns);
            for(int r=1; r<=8; r++){
                System.out.print(RESET_TEXT_COLOR);
                System.out.print(r + " ");
                for(int c=8; c>=1; c--){
                    buildBoard(board, r, c);
                }
                System.out.print(RESET_BG_COLOR);
                System.out.print(RESET_TEXT_COLOR);
                System.out.print(" " + r);
                System.out.println();
            }
            System.out.println(paddedColumns);
        }


    }

    private static void buildBoard(ChessBoard board, int r, int c) {
        if((r + c) % 2 == 1){
            System.out.print(SET_BG_COLOR_MAGENTA);
        } else{
            System.out.print(SET_BG_COLOR_BLACK);
        }
        if(board.getPiece(new ChessPosition(r, c)) == null){
            System.out.print(EMPTY);
        } else{
            System.out.print(getPiece(board.getPiece(new ChessPosition(r, c))));
        }
    }

    public static String getPiece(ChessPiece piece){
        boolean isWhite = false;
        String str = "";
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            isWhite = true;
            System.out.print(SET_TEXT_COLOR_RED);
        } else {
            System.out.print(SET_TEXT_COLOR_BLUE);
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            return isWhite ? WHITE_KING : BLACK_KING;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            return isWhite ? WHITE_QUEEN : BLACK_QUEEN;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            return isWhite ? WHITE_BISHOP : BLACK_BISHOP;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            return isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
            return isWhite ? WHITE_ROOK : BLACK_ROOK;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            return isWhite ? WHITE_PAWN : BLACK_PAWN;
        }
        return null;
    }


    static void main() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        System.out.println("WHITE PERSPECTIVE:");
        BoardDrawer.draw(board, ChessGame.TeamColor.WHITE);
        System.out.println();
        System.out.println("BLACK PERSPECTIVE:");
        BoardDrawer.draw(board, ChessGame.TeamColor.BLACK);
    }
}

