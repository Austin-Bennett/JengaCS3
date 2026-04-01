package game.utils;

public class ArrayUtils {

    public static class ArrayWrapper<A> {
        private A data;
        private int length;

        private ArrayWrapper(A d, int l) {
            this.data = d;
            this.length = l;
        }

        public A getData() {
            return data;
        }
    }


    public static ArrayWrapper<float[]> wrap(float[] ray) {
        return new ArrayWrapper<>(ray, ray.length);
    }
    public static float[] unwrap(ArrayWrapper<float[]> ray) {
        float[] res = new float[ray.length];
        System.arraycopy(ray.data, 0, res, 0, res.length);

        return res;
    }

    public static void append(ArrayWrapper<float[]> wrapper, float a) {

        if (wrapper.length == wrapper.data.length) {
            float[] n_alloc = new float[wrapper.length * 2 + 1];

            System.arraycopy(wrapper.data, 0, n_alloc, 0, wrapper.length);
            wrapper.data = n_alloc;

        }

        wrapper.data[wrapper.length++] = a;
    }

    public static void appendAll(ArrayWrapper<float[]> wrapper, float[] elements) {
        for (float i: elements) {
            append(wrapper, i);
        }
    }


}
