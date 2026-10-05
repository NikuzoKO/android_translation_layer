package android.graphics;

import android.content.res.AssetManager;
import java.io.File;

/* Family (null = the default sans-serif, "serif" or "monospace"), weight (100..900) and italic.
   Android's family names carry the weight ("sans-serif-medium", "sans-serif-light", ...). TextView
   applies these as CSS classes, Paint as a Pango font description. */
public class Typeface {

	// Style
	public static final int NORMAL = 0;
	public static final int BOLD = 1;
	public static final int ITALIC = 2;
	public static final int BOLD_ITALIC = 3;

	public static final Typeface DEFAULT = new Typeface(null, 400, false);
	public static final Typeface DEFAULT_BOLD = new Typeface(null, 700, false);
	public static final Typeface SANS_SERIF = new Typeface(null, 400, false);
	public static final Typeface SERIF = new Typeface("serif", 400, false);
	public static final Typeface MONOSPACE = new Typeface("monospace", 400, false);

	public long native_instance = 0; // directly accessed by androidx

	private final String family;
	private final int weight;
	private final boolean italic;

	public Typeface() {
		this(null, 400, false);
	}

	private Typeface(String family, int weight, boolean italic) {
		this.family = family;
		this.weight = weight;
		this.italic = italic;
	}

	/* the generic family for Pango/CSS, or null for the default (sans-serif) */
	public String getFamily() {
		return family;
	}

	/* family names ("sans-serif-medium") as well as font file/resource names ("res/font/roboto_medium.ttf") */
	private static Typeface fromName(String name, int style) {
		String family = null;
		int weight = 400;
		boolean italic = false;
		if (name != null) {
			name = name.toLowerCase().replace('_', '-');
			name = name.substring(name.lastIndexOf('/') + 1);
			if (name.lastIndexOf('.') > 0)
				name = name.substring(0, name.lastIndexOf('.'));
			if (name.startsWith("serif") || name.contains("-serif-") && !name.startsWith("sans"))
				family = "serif";
			else if (name.contains("mono"))
				family = "monospace";
			if (name.contains("thin"))
				weight = 100;
			else if (name.contains("extralight"))
				weight = 200;
			else if (name.contains("light"))
				weight = 300;
			else if (name.contains("medium"))
				weight = 500;
			else if (name.contains("semibold"))
				weight = 600;
			else if (name.contains("extrabold"))
				weight = 800;
			else if (name.contains("bold"))
				weight = 700;
			else if (name.contains("black") || name.contains("heavy"))
				weight = 900;
			italic = name.contains("italic");
		}
		if ((style & BOLD) != 0)
			weight = Math.max(weight, 700);
		return new Typeface(family, weight, italic || (style & ITALIC) != 0);
	}

	public static Typeface createFromAsset(AssetManager mgr, String path) {
		return fromName(path, NORMAL);
	}

	public static Typeface create(String family_name, int style) {
		return fromName(family_name, style);
	}

	public static Typeface create(Typeface typeface, int style) {
		if (typeface == null)
			typeface = DEFAULT;
		int weight = typeface.weight;
		if ((style & BOLD) != 0)
			weight = Math.max(weight, 700);
		else if (weight >= 700 && typeface.weight == 700)
			weight = 400;
		return new Typeface(typeface.family, weight, (style & ITALIC) != 0);
	}

	public static Typeface create(Typeface typeface, int weight, boolean italic) {
		return new Typeface(typeface != null ? typeface.family : null, Math.max(1, Math.min(1000, weight)), italic);
	}

	public static Typeface createFromFile(String path) {
		return fromName(path, NORMAL);
	}

	public static Typeface createFromFile(File file) {
		return createFromFile(file != null ? file.getName() : null);
	}

	public int getStyle() {
		return (weight >= 600 ? BOLD : 0) | (italic ? ITALIC : 0);
	}

	public final boolean isBold() {
		return weight >= 600;
	}

	public boolean isItalic() {
		return italic;
	}

	public int getWeight() {
		return weight;
	}

	public static Typeface defaultFromStyle(int style) {
		return create((String)null, style);
	}

	public static Typeface createFromFamiliesWithDefault(FontFamily[] families) {
		return DEFAULT;
	}

	public static Typeface createFromFamiliesWithDefault(FontFamily[] families, int dummy1, int dummy2) {
		return createFromFamiliesWithDefault(families);
	}

	public static class Builder {
		private String path;
		private int weight = -1;
		private int italic = -1;

		public Builder(AssetManager mgr, String path) {
			this.path = path;
		}

		public Builder(java.io.File file) {
			this.path = file != null ? file.getName() : null;
		}

		public Builder(java.io.FileDescriptor fd) {}

		public Builder setFontVariationSettings(String settings) {
			return this;
		}

		public Builder setFontVariationSettings(android.graphics.fonts.FontVariationAxis[] axes) {
			return this;
		}

		public Builder setItalic(boolean italic) {
			this.italic = italic ? 1 : 0;
			return this;
		}

		public Builder setWeight(int weight) {
			this.weight = weight;
			return this;
		}

		public Typeface build() {
			Typeface tf = fromName(path, NORMAL);
			return create(tf, weight > 0 ? weight : tf.weight, italic >= 0 ? italic == 1 : tf.italic);
		}
	}
}
