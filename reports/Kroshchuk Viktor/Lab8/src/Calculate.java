import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Calculate extends Application {

    double currentValue = 0;
    int currentIteration = 0;
    Text sum = new Text("");
    Thread backgroundThread;
    TextField inputCount = new TextField();
    TextField inp = new TextField();
    TextField in = new TextField();
    Button start = new Button();
    GridPane grid;

    @Override
    public void init() {
        start.setText("Начать");
        Button pause = new Button();
        pause.setText("Пауза");
        Button stop = new Button();
        stop.setText("Очистить");
        start.setOnAction(actionEvent -> startCalculate());
        pause.setOnAction(actionEvent -> {
            start.setDisable(false);
            backgroundThread.suspend(); // приостанавливает
        });
        stop.setOnAction(actionEvent -> {
            start.setDisable(false);
            stopCalculate();
        });
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text text = new Text("Текущая сумма: ");
        grid.add(text, 0, 0, 1, 1);
        grid.add(sum, 1, 0, 1, 1);
        Label labelCount = new Label("K:");
        grid.add(labelCount, 0, 1, 1, 1);
        grid.add(inputCount, 1, 1, 1, 1);
        Label labelinp = new Label("X:");
        grid.add(labelinp, 0,2,1,1);
        grid.add(inp, 1,2,1,1);
        Label labelin = new Label("A: ");
        grid.add(labelin, 0,3,1,1);
        grid.add(in, 1,3,1,1);
        grid.add(start, 0, 5);
        grid.add(pause, 1,5);
        grid.add(stop, 2, 5);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Function");
        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startCalculate() {
        if (backgroundThread != null) {
            backgroundThread.resume(); // продолжает
        } else{
            Thread task = new Thread(() -> {
                try {
                    int k = Integer.parseInt(inputCount.getText()) + 1;
                    int x = Integer.parseInt(inp.getText());
                    int a = Integer.parseInt(in.getText());
                    start.setDisable(true);
                    if(a == 0)
                        sum.setText("-infinity");
                    if (k == 0) {
                        sum.setText(Double.toString(1.0));
                    } else {
                        sum.setText(Double.toString(this.currentValue));
                        for (int i = 0; i < k; i++) {
                            try {
                                this.currentValue += Math.pow(x * Math.log(a), i) / factorial(i);
                                Thread.sleep(500);
                                sum.setText(Double.toString(this.currentValue));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    start.setDisable(false);
                } catch (NumberFormatException e) {
                    sum.setText("Ошибка ввода");
                }
            });
            backgroundThread = new Thread(task);
            backgroundThread.setDaemon(true);
            backgroundThread.start();
        }
    }

    public double factorial(int num){
        if(num == 0 || num == 1)
            return 1;
        int sum = 1;
        for(int i = 2; i <= num; i++)
            return sum *= i;
        return sum;
    }

    public void stopCalculate() {
        backgroundThread.stop();
        this.currentValue = 0;
        this.sum.setText("");
        this.currentIteration = 0;
        inputCount.setText("");
        inp.setText("");
        in.setText("");
        backgroundThread = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
