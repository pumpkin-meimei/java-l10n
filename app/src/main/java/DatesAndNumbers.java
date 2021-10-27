import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class DatesAndNumbers extends SceneController {
    private static final int MAX_NUMBER_INPUT_LENGTH = 64;

    private final ResourceBundle messages = ResourceBundle.getBundle("datesAndNumbers", Locale.getDefault());
    private final MessageFormat dateOffsetFromTodayFormatter = new MessageFormat(messages.getString("dateOffsetFromToday"));
    private final MessageFormat todayIsFormatter = new MessageFormat(messages.getString("todayIs"));
    private final ObjectProperty<ZonedDateTime> currentZonedDateTime = new SimpleObjectProperty<>();
    private final Timeline timeline;

    public TextField dateOffset;
    public TextField dividend;
    public TextField divisor;
    public Text dateOffsetFromToday;
    public Label dividendFormatted;
    public Label divisorFormatted;
    public Label outputFormatted;
    public Label todayIsLabel;

    public DatesAndNumbers() {
        timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                event -> currentZonedDateTime.set(ZonedDateTime.now())
            ),
            new KeyFrame(Duration.millis(500))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void initialize() {
        restrictToIntegers(dateOffset);
        restrictToIntegers(dividend);
        restrictToIntegers(divisor);

        NumberFormat numberFormatter = NumberFormat.getInstance();
        dividendFormatted.textProperty()
            .bind(Bindings.createStringBinding(
                () -> dividend.textProperty().isEmpty().get() ?
                    "" :
                    numberFormatter.format(
                        new BigDecimal(dividend.textProperty().get())),
                dividend.textProperty()));
        divisorFormatted.textProperty()
            .bind(Bindings.createStringBinding(
                () -> divisor.textProperty().isEmpty().get() ?
                    "" :
                    numberFormatter.format(
                        new BigDecimal(divisor.textProperty().get())),
                divisor.textProperty()));
        outputFormatted.textProperty()
            .bind(Bindings.createStringBinding(
                () -> {
                    if (dividend.textProperty().isEmpty().get() || divisor.textProperty().isEmpty().get()) {
                        return "";
                    }
                    try {
                        return numberFormatter.format(
                            new BigDecimal(dividend.textProperty().get()).divide(
                                new BigDecimal(divisor.textProperty().get()), 3, RoundingMode.HALF_EVEN));
                    } catch (ArithmeticException e) {
                        System.err.println(e);
                        return "undefined";
                    }
                },
                dividend.textProperty(), divisor.textProperty()));

        todayIsLabel.textProperty()
            .bind(Bindings.createStringBinding(
                () -> {
                    ZonedDateTime current = currentZonedDateTime.get();
                    if (current == null) {
                        return "";
                    }

                    return todayIsFormatter.format(new Object[]{
                        current.format(
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG))
                    });
                }, currentZonedDateTime));

        dateOffsetFromToday.textProperty()
            .bind(Bindings.createStringBinding(
                () -> {
                    ZonedDateTime current = currentZonedDateTime.get();
                    if (current == null) {
                        return "";
                    }

                    BigDecimal days = dateOffset.textProperty().isEmpty().get() ?
                        BigDecimal.ZERO :
                        new BigDecimal(dateOffset.textProperty().get());

                    try {
                        return dateOffsetFromTodayFormatter.format(new Object[]{
                            days,
                            currentZonedDateTime.get().plusDays(days.longValue()).format(
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG))
                        });
                    }
                    catch (DateTimeException e) {
                        return messages.getString("offsetOutsideAllowedRange");
                    }
                }, dateOffset.textProperty(), currentZonedDateTime));

        timeline.play();
    }

    public void goBack() throws IOException {
        timeline.stop();

        appController.goToHome();
    }

    private void restrictToIntegers(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange() && change.getControlNewText().length() <= MAX_NUMBER_INPUT_LENGTH) {
                String changeText = change.getText();
                if (changeText.matches("\\d*")) {
                    return change;
                }
            }
            return null;
        }));
    }
}
