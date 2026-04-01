#version 330 core
out vec4 FragColor;

in vec3 pos;
in vec3 normal;
in vec2 uv;

float smoothing_func(float t) {


    return sqrt(t + 0.5);
}

bool feq(float a, float b) {
    return abs(a - b) <= 0.0001;
}

float hash(vec2 p) {
    p = fract(p * vec2(127.1, 311.7));
    p += dot(p, p + 19.19);
    return fract(p.x * p.y);
}

float noise(float x, float y) {
    vec2 p = vec2(x, y);
    vec2 i = floor(p);
    vec2 f = fract(p);

    // Sample the four corners of the cell
    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    // Smoothstep interpolation (C1 continuous)
    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float grainNoise(float x, float y) {
    vec2 p = vec2(x, y);
    p = fract(p * vec2(127.1, 311.7));
    p += dot(p, p + 19.19);
    return fract(p.x * p.y);
}

void main()
{
    vec3 inv_lightdir = normalize(vec3(1, 1, 1));



    vec3 color = vec3(0.75, 0.66, 0.29);

    color *= smoothing_func(dot(inv_lightdir, normal) * noise(uv.x, uv.y) + grainNoise(uv.x, uv.y) * 0.1);

    FragColor = vec4(color, 1);
}