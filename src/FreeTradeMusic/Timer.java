//package FreeTradeMusic;
//
//import javafx.application.Application;
//
///**
// * Created by josej on 5/21/2017.
// */
//public class Timer extends Application
//{
////    @Override public void start(final Stage stage) throws Exception
////    {
////        countdownButton.setOnAction(new EventHandler<ActionEvent>() {
////            @Override public void handle(ActionEvent t) {
////                countdownButton.setText("Restart");
////                countdown.start();
////            }
////        });
////    }
//}
//
//class CountDown {
////    private final ReadOnlyIntegerWrapper timeLeft;
////    private final ReadOnlyDoubleWrapper  timeLeftDouble;
////
////    public ReadOnlyIntegerProperty timeLeftProperty()
////    {
////        return timeLeft.getReadOnlyProperty();
////    }
////
////    public CountDown(final int time)
////    {
////        timeLeft       = new ReadOnlyIntegerWrapper(time);
////        timeLeftDouble = new ReadOnlyDoubleWrapper(time);
////
////        timeline = new Timeline
////                (
////                new KeyFrame(
////                        Duration.ZERO,
////                        new KeyValue(timeLeftDouble, time)
////                ),
////                new KeyFrame(
////                        Duration.seconds(time),
////                        new KeyValue(timeLeftDouble, 0)
////                )
////        );
////
////        timeLeftDouble.addListener(new InvalidationListener()
////        {
////            @Override public void invalidated(Observable o) {
////                timeLeft.set((int) Math.ceil(timeLeftDouble.get()));
////            }
////        });
////    }
////
////    public void start()
////    {
////        timeline.playFromStart();
////    }
//}
