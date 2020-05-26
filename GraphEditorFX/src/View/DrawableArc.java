package View;

import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import model.Arc;

import java.util.Objects;
import java.util.Random;

import static View.DrawableNode.CIRCLE_RADIUS;


public class DrawableArc {
    private static final Bloom BLOOM = new Bloom(0);

    private static final int LOOP_RADIUS = 50;
    private static final int LINE_WIDTH = 3;
    private static final int ARROW_SIDE = CIRCLE_RADIUS;
    private static final int ARROW_SIDE_TO_HEIGHT_ANGLE = 20;
    private static final double SIN_Y = Math.sin(Math.toRadians(ARROW_SIDE_TO_HEIGHT_ANGLE));
    private static final double COS_Y = Math.cos(Math.toRadians(ARROW_SIDE_TO_HEIGHT_ANGLE));

    private Arc sourceArc;
    private DrawableNode begin;
    private DrawableNode end;

    private boolean isFocused;

    public int key;

    private Polygon arrow;
    private Line line;
    private CubicCurve loop;
    private Color color;
    private QuadCurve curve;

    // Properties for arrow correct rotating and locating
    private double headX;
    private double headY;
    private double leftX;
    private double leftY;
    private double rightX;
    private double rightY;

    private double headXMod;
    private double headYMod;
    private double leftXMod;
    private double leftYMod;
    private double rightXMod;
    private double rightYMod;

    private double cos;
    private double sin;


    public DrawableArc(Arc sourceArc, DrawableNode begin, DrawableNode end) {
        this.sourceArc = sourceArc;
        this.begin = begin;
        this.end = end;

        isFocused = false;

        Random randomColorComponent = new Random(System.currentTimeMillis());

        color = Color.color(
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble()
        );

        arrow = new Polygon();

        if (begin.getSourceNode().equals(end.getSourceNode())) {
            loop = new CubicCurve();
            configureLoop();

            line = new Line();
        } else {
            configureArrow();

            line = new Line(
                    begin.getShape().getCenterX(), begin.getShape().getCenterY(),
                    end.getShape().getCenterX(), end.getShape().getCenterY()
            );
            configureLine();

            loop = new CubicCurve();
        }
    }

//////////////////////////////////////////////////////////////////////////////////////
    public DrawableArc(Arc sourceArc, DrawableNode begin, DrawableNode end, int key) {
        this.sourceArc = sourceArc;
        this.begin = begin;
        this.end = end;
        this.key=key;


        isFocused = false;

        Random randomColorComponent = new Random(System.currentTimeMillis());

        color = Color.color(
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble()
        );

        arrow = new Polygon();

        if (begin.getSourceNode().equals(end.getSourceNode())) {
            loop = new CubicCurve();
            configureLoop();

            line = new Line();
        } else {
            configureArrowCurve();


            curve = new QuadCurve(
                    begin.getShape().getCenterX(), begin.getShape().getCenterY(),
                    (begin.getShape().getCenterX()+end.getShape().getCenterX())/2+50*key,(begin.getShape().getCenterY()+end.getShape().getCenterY())/2+50*key,
                    end.getShape().getCenterX(), end.getShape().getCenterY()
            );

            key++;

            /*line = new Line(
                    begin.getShape().getCenterX(), begin.getShape().getCenterY(),
                    end.getShape().getCenterX(), end.getShape().getCenterY()
            );
            configureLine();*/
            configureCurve();

            line = new Line();
            loop = new CubicCurve();
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////


    public DrawableNode getBegin() {
        return begin;
    }

    public DrawableNode getEnd() {
        return end;
    }

    public Polygon getArrow() {
        return arrow;
    }

    public Line getLine() {
        return line;
    }

    public QuadCurve getCurve() {return curve;}

    public CubicCurve getLoop() {
        return loop;
    }

    public Arc getSourceArc() {
        return sourceArc;
    }

    public boolean isFocused() {
        return isFocused;
    }

    //---------------------------------------------------КРАТНЫЕ ДУГИ--------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrawableArc that = (DrawableArc) o;
        return Objects.equals(begin, that.begin) &&
                Objects.equals(end, that.end);
    }

    /*
        Configs
     */

    // Line configs: bindings, coloring
    private void configureLine() {
        line.setStrokeWidth(LINE_WIDTH);
        line.setStroke(color);

        line.startXProperty().bind(begin.getShape().centerXProperty());
        line.startYProperty().bind(begin.getShape().centerYProperty());
        line.endXProperty().bind(end.getShape().centerXProperty());
        line.endYProperty().bind(end.getShape().centerYProperty());


        // Line lightning when mouse entered
        line.setOnMouseEntered(e -> {
            line.setEffect(BLOOM);
            arrow.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        line.setOnMouseExited(e -> {
            line.setEffect(null);
            arrow.setEffect(null);
            isFocused = false;
        });
    }


    private void configureCurve() {
        curve.setStrokeWidth(LINE_WIDTH);
        curve.setStroke(color);

        curve.startXProperty().bind(begin.getShape().centerXProperty());
        curve.startYProperty().bind(begin.getShape().centerYProperty());
        curve.endXProperty().bind(end.getShape().centerXProperty());
        curve.endYProperty().bind(end.getShape().centerYProperty());
        //curve.controlYProperty().bind();
        //curve.endXProperty().bind();


        curve.setFill(Color.TRANSPARENT);

        // Line lightning when mouse entered
        curve.setOnMouseEntered(e -> {
            curve.setEffect(BLOOM);
            arrow.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        curve.setOnMouseExited(e -> {
            curve.setEffect(null);
            arrow.setEffect(null);
            isFocused = false;
        });
    }




    // Loop configs: binding, coloring
    private void configureLoop() {
        loop.setStrokeWidth(LINE_WIDTH);
        loop.setFill(Color.TRANSPARENT);
        loop.setStroke(color);

        loop.startXProperty().bind(begin.getShape().centerXProperty());
        loop.startYProperty().bind(begin.getShape().centerYProperty());
        loop.endXProperty().bind(end.getShape().centerXProperty());
        loop.endYProperty().bind(end.getShape().centerYProperty());
        loop.controlX1Property().bind(begin.getShape().centerXProperty().add(-LOOP_RADIUS));
        loop.controlY1Property().bind(begin.getShape().centerYProperty().add(-LOOP_RADIUS));
        loop.controlX2Property().bind(end.getShape().centerXProperty().add(-LOOP_RADIUS));
        loop.controlY2Property().bind(end.getShape().centerYProperty().add(LOOP_RADIUS));

        // Line lightning when mouse entered
        loop.setOnMouseEntered(e -> {
            loop.setEffect(BLOOM);
            arrow.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        loop.setOnMouseExited(e -> {
            loop.setEffect(null);
            arrow.setEffect(null);
            isFocused = false;
        });
    }

    // Arrow configs: events handling, coloring
    private void configureArrow() {
        arrow.setStrokeWidth(LINE_WIDTH);
        arrow.setFill(color);
        arrow.setStroke(color);

        updateArrowShape();

        begin.getShape().centerXProperty().addListener(change -> {
            updateArrowShape();
        });
        begin.getShape().centerYProperty().addListener(change -> {
            updateArrowShape();
        });
        end.getShape().centerXProperty().addListener(change -> {
            updateArrowShape();
        });
        end.getShape().centerYProperty().addListener(change -> {
            updateArrowShape();
        });

        // Arrow lightning when mouse entered
        arrow.setOnMouseEntered(e -> {
            arrow.setEffect(BLOOM);
            line.setEffect(BLOOM);
            loop.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        arrow.setOnMouseExited(e -> {
            arrow.setEffect(null);
            line.setEffect(null);
            loop.setEffect(null);
            isFocused = false;
        });
    }


    private void configureArrowCurve() {
        arrow.setStrokeWidth(LINE_WIDTH);
        arrow.setFill(color);
        arrow.setStroke(color);

        updateArrowShape();

        begin.getShape().centerXProperty().addListener(change -> {
            updateArrowShapeCurve();
        });
        begin.getShape().centerYProperty().addListener(change -> {
            updateArrowShapeCurve();
        });
        end.getShape().centerXProperty().addListener(change -> {
            updateArrowShapeCurve();
        });
        end.getShape().centerYProperty().addListener(change -> {
            updateArrowShapeCurve();
        });

        // Arrow lightning when mouse entered
        arrow.setOnMouseEntered(e -> {
            arrow.setEffect(BLOOM);
            line.setEffect(BLOOM);
            loop.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        arrow.setOnMouseExited(e -> {
            arrow.setEffect(null);
            line.setEffect(null);
            loop.setEffect(null);
            isFocused = false;
        });
    }

    private void updateArrowTransformCurve() {
        // cos = |endX - startX| / sqrt((endX - startX)^2 + (endY - startY)^2)
        cos = Math.abs(end.getShape().getCenterX() - begin.getShape().getCenterX())
                / Math.sqrt(Math.pow(end.getShape().getCenterX() - begin.getShape().getCenterX(), 2)
                + Math.pow(end.getShape().getCenterY() - begin.getShape().getCenterY(), 2));

        // sin = sqrt(1 - cos^2)
        sin = Math.sqrt(1 - Math.pow(cos, 2));


        headXMod = CIRCLE_RADIUS * cos;
        headYMod = CIRCLE_RADIUS * sin;

        headX = end.getShape().getCenterX() > begin.getShape().getCenterX() ?
                end.getShape().getCenterX() - headXMod
                : end.getShape().getCenterX() + headXMod;

        headY = end.getShape().getCenterY() > begin.getShape().getCenterY() ?
                end.getShape().getCenterY() - headYMod
                : end.getShape().getCenterY() + headYMod;

        // sin (90 - a - y) = cos a * COS_Y - sin a * SIN_Y &&& cos (90 - a - y) = sin a * COS_Y + cos a * SIN_Y =>>>
        rightXMod = ARROW_SIDE * (cos * COS_Y - sin * SIN_Y);
        rightYMod = ARROW_SIDE * (sin * COS_Y + cos * SIN_Y);

        rightX = end.getShape().getCenterX() > begin.getShape().getCenterX() ?
                headX - rightXMod
                : headX + rightXMod;
        rightY = end.getShape().getCenterY() > begin.getShape().getCenterY() ?
                headY - rightYMod
                : headY + rightYMod;

        //  cos (a - y) = cos a * COS_Y + sin a * SIN_Y &&& sin (a - y) = sin a * COS_Y - cos a * SIN_Y
        leftXMod = ARROW_SIDE * (cos * COS_Y + sin * SIN_Y);
        leftYMod = ARROW_SIDE * (sin * COS_Y - cos * SIN_Y);

        leftX = end.getShape().getCenterX() > begin.getShape().getCenterX() ?
                headX - leftXMod
                : headX + leftXMod;
        leftY = end.getShape().getCenterY() > begin.getShape().getCenterY() ?
                headY - leftYMod
                : headY + leftYMod;
    }

    private void updateArrowShapeCurve() {
        updateArrowTransformCurve();

        arrow.getPoints().clear();
        arrow.getPoints().addAll(
                headX, headY,
                leftX, leftY,
                rightX, rightY,
                headX, headY
        );
    }


    // Updates arrow coordinates if incident node was moved
    private void updateArrowTransform() {
        // cos = |endX - startX| / sqrt((endX - startX)^2 + (endY - startY)^2)
        cos = Math.abs(end.getShape().getCenterX() - begin.getShape().getCenterX())
                / Math.sqrt(Math.pow(end.getShape().getCenterX() - begin.getShape().getCenterX(), 2)
                + Math.pow(end.getShape().getCenterY() - begin.getShape().getCenterY(), 2));

        // sin = sqrt(1 - cos^2)
        sin = Math.sqrt(1 - Math.pow(cos, 2));


        headXMod = CIRCLE_RADIUS * cos;
        headYMod = CIRCLE_RADIUS * sin;

        headX = end.getShape().getCenterX() > begin.getShape().getCenterX() ?
                end.getShape().getCenterX() - headXMod
                : end.getShape().getCenterX() + headXMod;

        headY = end.getShape().getCenterY() > begin.getShape().getCenterY() ?
                end.getShape().getCenterY() - headYMod
                : end.getShape().getCenterY() + headYMod;

        // sin (90 - a - y) = cos a * COS_Y - sin a * SIN_Y &&& cos (90 - a - y) = sin a * COS_Y + cos a * SIN_Y =>>>
        rightXMod = ARROW_SIDE * (cos * COS_Y - sin * SIN_Y);
        rightYMod = ARROW_SIDE * (sin * COS_Y + cos * SIN_Y);

        rightX = end.getShape().getCenterX() > begin.getShape().getCenterX() ?
                headX - rightXMod
                : headX + rightXMod;
        rightY = end.getShape().getCenterY() > begin.getShape().getCenterY() ?
                headY - rightYMod
                : headY + rightYMod;

        //  cos (a - y) = cos a * COS_Y + sin a * SIN_Y &&& sin (a - y) = sin a * COS_Y - cos a * SIN_Y
        leftXMod = ARROW_SIDE * (cos * COS_Y + sin * SIN_Y);
        leftYMod = ARROW_SIDE * (sin * COS_Y - cos * SIN_Y);

        leftX = end.getShape().getCenterX() > begin.getShape().getCenterX() ?
                headX - leftXMod
                : headX + leftXMod;
        leftY = end.getShape().getCenterY() > begin.getShape().getCenterY() ?
                headY - leftYMod
                : headY + leftYMod;
    }

    // Redraws arrow polygon
    private void updateArrowShape() {
        updateArrowTransform();

        arrow.getPoints().clear();
        arrow.getPoints().addAll(
                headX, headY,
                leftX, leftY,
                rightX, rightY,
                headX, headY
        );
    }
}
