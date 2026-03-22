#version 330 core
out vec4 FragColor;

in vec3 pos;
in vec3 normal;
in vec2 uv;

float smoothing_func(float t) {


    return t;
}

bool feq(float a, float b) {
    return abs(a - b) <= 0.0001;
}

void main()
{
    vec3 inv_lightdir = normalize(vec3(1, 1, 1));



    vec3 color = vec3(0.75, 0.66, 0.29);

    color *= clamp(dot(inv_lightdir, normal), 0.25, 1) * 2;

    FragColor = vec4(color, 1);
}