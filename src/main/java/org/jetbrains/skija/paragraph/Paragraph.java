package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.*;

import java.util.Objects;

public class Paragraph extends Managed {
    public enum Affinity {
        UPSTREAM,
        DOWNSTREAM
    }

    public enum RectHeightStyle {

        /** Provide tight bounding boxes that fit heights per run. */
        TIGHT,

        /**
         * The height of the boxes will be the maximum height of all runs in the
         * line. All rects in the same line will be the same height.
         */
        MAX,

        /**
         * Extends the top and/or bottom edge of the bounds to fully cover any line
         * spacing. The top edge of each line should be the same as the bottom edge
         * of the line above. There should be no gaps in vertical coverage given any
         * ParagraphStyle line_height.
         *
         * The top and bottom of each rect will cover half of the
         * space above and half of the space below the line.
         */
        INCLUDE_LINE_SPACING_MIDDLE,

        /** The line spacing will be added to the top of the rect. */
        INCLUDE_LINE_SPACING_TOP,

        /** The line spacing will be added to the bottom of the rect. */
        INCLUDE_LINE_SPACING_BOTTOM,

        STRUT
    }

    public enum RectWidthStyle {

        /** Provide tight bounding boxes that fit widths to the runs of each line independently. */
        TIGHT,

        /** Extends the width of the last rect of each line to match the position of the widest rect over all the lines. */
        MAX
    }

    public enum TextDirection {
        RTL,
        LTR
    }

    public static class PositionWithAffinity {
        public final int position;
        public final Affinity affinity;

        public PositionWithAffinity(int position, Affinity affinity) {
            this.position = position;
            this.affinity = affinity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionWithAffinity that = (PositionWithAffinity) o;
            return position == that.position &&
                    affinity.equals(that.affinity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, affinity);
        }

        @Override
        public String toString() {
            return "PositionWithAffinity{" +
                    "position=" + position +
                    ", affinity=" + affinity +
                    '}';
        }
    }

    public static class TextBox {
        public final Rect rect;
        public final TextDirection direction;

        public TextBox(Rect rect, TextDirection direction) {
            this.rect = rect;
            this.direction = direction;
        }

        public TextBox(float l, float t, float r, float b, int direction) {
            this.rect = Rect.makeLTRB(l, t, r, b);
            this.direction = TextDirection.values()[direction];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TextBox textBox = (TextBox) o;
            return rect.equals(textBox.rect) &&
                    direction == textBox.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rect, direction);
        }

        @Override
        public String toString() {
            return "TextBox{" +
                    "rect=" + rect +
                    ", direction=" + direction +
                    '}';
        }
    }

    public enum TextAlign {
        LEFT,
        RIGHT,
        CENTER,
        JUSTIFY,
        START,
        END,
    }

    public float getMaxWidth() {
        Stats.onNativeCall();
        return nGetMaxWidth(_ptr);
    }

    public float getHeight() {
        Stats.onNativeCall();
        return nGetHeight(_ptr);
    }

    public float getMinIntrinsicWidth() {
        Stats.onNativeCall();
        return nGetMinIntrinsicWidth(_ptr);
    }

    public float getMaxIntrinsicWidth() {
        Stats.onNativeCall();
        return nGetMaxIntrinsicWidth(_ptr);
    }

    public float getAlphabeticBaseline() {
        Stats.onNativeCall();
        return nGetAlphabeticBaseline(_ptr);
    }

    public float getIdeographicBaseline() {
        Stats.onNativeCall();
        return nGetIdeographicBaseline(_ptr);
    }

    public float getLongestLine() {
        Stats.onNativeCall();
        return nGetLongestLine(_ptr);
    }

    public boolean didExceedMaxLines() {
        Stats.onNativeCall();
        return nDidExceedMaxLines(_ptr);
    }

    public Paragraph layout(float width) {
        Stats.onNativeCall();
        nLayout(_ptr, width);
        return this;
    }

    public Paragraph paint(Canvas canvas, float x, float y) {
        Stats.onNativeCall();
        nPaint(_ptr, Native.getPtr(canvas), x, y);
        return this;
    }

    /**
     * Returns a vector of bounding boxes that enclose all text between
     * start and end glyph indexes, including start and excluding end.
     */
    public TextBox[] getRectsForRange(int start, int end, RectHeightStyle rectHeightStyle, RectWidthStyle rectWidthStyle) {
        Stats.onNativeCall();
        return nGetRectsForRange(_ptr, start, end, rectHeightStyle.ordinal(), rectWidthStyle.ordinal());
    }

    public TextBox[] getRectsForPlaceholders() {
        Stats.onNativeCall();
        return nGetRectsForPlaceholders(_ptr);
    }

    public PositionWithAffinity getGlyphPositionAtCoordinate(float dx, float dy) {
        Stats.onNativeCall();
        int res = nGetGlyphPositionAtCoordinate(_ptr, dx, dy);
        if (res >= 0)
            return new PositionWithAffinity(res, Affinity.DOWNSTREAM);
        else
            return new PositionWithAffinity(-res-1, Affinity.UPSTREAM);
    }

    public IRange getWordBoundary(int offset) {
        Stats.onNativeCall();
        long l = nGetWordBoundary(_ptr, offset);
        return new IRange((int) (l >>> 32), (int) (l & 0xFFFFFFFF));
    }

    public LineMetrics[] getLineMetrics() {
        Stats.onNativeCall();
        return nGetLineMetrics(_ptr);
    }

    public long lineNumber() {
        Stats.onNativeCall();
        return nLineNumber(_ptr);
    }

    public Paragraph markDirty() {
        Stats.onNativeCall();
        nMarkDirty(_ptr);
        return this;
    }

    public int unresolvedGlyphs() {
        Stats.onNativeCall();
        return nUnresolvedGlyphs(_ptr);
    }

    public Paragraph updateTextAlign(TextAlign align) {
        Stats.onNativeCall();
        nUpdateTextAlign(_ptr, align.ordinal());
        return this;
    }

    public Paragraph updateText(int from, String text) {
        Stats.onNativeCall();
        nUpdateText(_ptr, from, text);
        return this;
    }

    public Paragraph updateFontSize(int from, int to, float size) {
        Stats.onNativeCall();
        nUpdateFontSize(_ptr, from, to, size);
        return this;
    }

    public Paragraph updateForegroundPaint(int from, int to, Paint paint) {
        Stats.onNativeCall();
        nUpdateForegroundPaint(_ptr, from, to, Native.getPtr(paint));
        return this;
    }

    public Paragraph updateBackgroundPaint(int from, int to, Paint paint) {
        Stats.onNativeCall();
        nUpdateBackgroundPaint(_ptr, from, to, Native.getPtr(paint));
        return this;
    }

    protected Paragraph(long ptr) { super(ptr, nativeFinalizer); Stats.onNativeCall(); }
    private static final  long          nativeFinalizer = nGetNativeFinalizer();
    private static native long          nGetNativeFinalizer();
    private static native float         nGetMaxWidth(long ptr);
    private static native float         nGetHeight(long ptr);
    private static native float         nGetMinIntrinsicWidth(long ptr);
    private static native float         nGetMaxIntrinsicWidth(long ptr);
    private static native float         nGetAlphabeticBaseline(long ptr);
    private static native float         nGetIdeographicBaseline(long ptr);
    private static native float         nGetLongestLine(long ptr);
    private static native boolean       nDidExceedMaxLines(long ptr);
    private static native void          nLayout(long ptr, float width);
    private static native long          nPaint(long ptr, long canvasPtr, float x, float y);
    private static native TextBox[]     nGetRectsForRange(long ptr, int start, int end, int rectHeightStyle, int rectWidthStyle);
    private static native TextBox[]     nGetRectsForPlaceholders(long ptr);
    private static native int           nGetGlyphPositionAtCoordinate(long ptr, float dx, float dy);
    private static native long          nGetWordBoundary(long ptr, int offset);
    private static native LineMetrics[] nGetLineMetrics(long ptr);
    private static native long          nLineNumber(long ptr);
    private static native void          nMarkDirty(long ptr);
    private static native int           nUnresolvedGlyphs(long ptr);
    private static native void          nUpdateTextAlign(long ptr, int textAlign);
    private static native void          nUpdateText(long ptr, int from, String text);
    private static native void          nUpdateFontSize(long ptr, int from, int to, float size);
    private static native void          nUpdateForegroundPaint(long ptr, int from, int to, long paintPtr);
    private static native void          nUpdateBackgroundPaint(long ptr, int from, int to, long paintPtr);
}
