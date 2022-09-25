package com.example.tictactoev2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    //    Private variables
    private TicTacToe tttGame;
    private TextView status;

    //    private ButtonsGridAndTextView tttView;
    private Button[][] buttons;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Building gui and setting a new game
        tttGame = new TicTacToe();
        buildGuiByCode();
    }


    public void buildGuiByCode() {



        //Getting width of the screen
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int w = size.x / TicTacToe.SIDE;



        //Creating the layout manager as a gridlayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(TicTacToe.SIDE);
        gridLayout.setRowCount(TicTacToe.SIDE + 1);



        //Creating buttons dynamically and changing the size
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler();



        //Creating buttons, setting size, and setting an onclick listener
        for (int row = 0; row < TicTacToe.SIDE; row++) {
            for (int col = 0; col < TicTacToe.SIDE; col++) {
                buttons[row][col] = new Button(this);
                buttons[row][col].setTextSize((int) (w * .2));
                buttons[row][col].setOnClickListener(bh);
                gridLayout.addView(buttons[row][col], w, w);
            }
        }



        //Setting up layout parameters
        status = new TextView(this);

        GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE, 1);
        GridLayout.Spec columnSpec = GridLayout.spec(0, TicTacToe.SIDE);

        GridLayout.LayoutParams lpStatus
                = new GridLayout.LayoutParams(rowSpec, columnSpec);
        status.setLayoutParams(lpStatus);


        //Making the width the same size of the button "Dynamically"
        status.setWidth(TicTacToe.SIDE * w);
        status.setHeight(w);
        status.setGravity(Gravity.CENTER);
        status.setBackgroundColor(Color.GREEN);
        status.setTextSize((int) (w * .1));
        status.setText(tttGame.result());

        gridLayout.addView(status);


        setContentView(gridLayout);
    }






    public void resetButtons() {
        for (int row = 0; row < TicTacToe.SIDE; row++)
            for (int col = 0; col < TicTacToe.SIDE; col++)
                buttons[row][col].setText("");
    }



    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            Log.w("MainActivity", "Inside OnLClick, v = " + v);
            for (int row = 0; row < TicTacToe.SIDE; row++)
                for (int column = 0; column < TicTacToe.SIDE; column++)
                    if (v == buttons[row][column])
                        update(row, column);

        }

    }




    //Setting an X or O depending on which user clicks
    public void update(int row, int column) {
        int play = tttGame.play(row, column);
        if (play == 1)
            buttons[row][column].setText("X");
        else if (play == 2)
            buttons[row][column].setText("O");
        if (tttGame.isGameOver()) {

            status.setBackgroundColor(Color.RED);
            enableButtons(false);
            status.setText(tttGame.result());
            showNewGameDialog();
        }

    }




    private void showNewGameDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play Again?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("Yes", playAgain);
        alert.setNegativeButton("No", playAgain);
        alert.show();

    }




    private void enableButtons(boolean enabled) {

        for (int row = 0; row < TicTacToe.SIDE; row++)
            for (int col = 0; col < TicTacToe.SIDE; col++)
                buttons[row][col].setEnabled(enabled);

    }




    private class PlayDialog implements DialogInterface.OnClickListener {


        @Override
        public void onClick(DialogInterface dialog, int id) {
            if(id == -1){
                tttGame.resetGame();
                enableButtons(true);
                status.setBackgroundColor(Color.GREEN);
                resetButtons();
            } else if(id == -2)
                MainActivity.this.finish();

            }
        }
    }


