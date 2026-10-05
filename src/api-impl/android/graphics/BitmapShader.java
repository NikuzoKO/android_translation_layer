package android.graphics;

public class BitmapShader extends Shader {

	Bitmap bitmap;
	Matrix localMatrix;

	public BitmapShader(Bitmap bitmap, TileMode tileX, TileMode tileY) {
		this.bitmap = bitmap;
	}

	@Override
	public void setLocalMatrix(Matrix matrix) {
		localMatrix = matrix != null && !matrix.isIdentity() ? new Matrix(matrix) : null;
	}

	/* ATL: tiles are drawn repeated (TileMode.REPEAT); only scale and translation of the local matrix are honored */
	public Bitmap atlBitmap() {
		return bitmap;
	}

	public float[] atlTileTransform() {
		float[] v = new float[9];
		if (localMatrix == null)
			return new float[] {1, 1, 0, 0};
		localMatrix.getValues(v);
		return new float[] {v[Matrix.MSCALE_X], v[Matrix.MSCALE_Y], v[Matrix.MTRANS_X], v[Matrix.MTRANS_Y]};
	}
}
