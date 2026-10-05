#include "pango/pango-layout.h"
#include <gdk/gdk.h>
#include <gsk/gsk.h>
#include <pango/pango.h>

struct AndroidPaint {
	GdkRGBA color;
	GskStroke *gsk_stroke;
	PangoFontDescription *font;
	PangoAlignment alignment;
	graphene_matrix_t color_matrix;
	graphene_vec4_t color_offset;
	bool is_fill : 1;
	bool is_stroke : 1;
	bool use_color_filter : 1;
	bool hairline : 1; /* stroke width 0 (Android's default): drawn 1 px wide, reported as 0 */
};
