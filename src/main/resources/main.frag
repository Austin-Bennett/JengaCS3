#version 330 core
out vec4 FragColor;

in vec3 pos;
in vec3 normal;
in vec2 uv;

float smoothing_func(float t) {


    return t;
}

void main()
{

    vec3 color = vec3(0.75, 0.66, 0.29);

    if (normal.z == 1 || normal.z == -1) {
        color *= 0.75;
    }

    if (normal.x == 1 || normal.x == -1) {
        color *= 0.85;
    }

    FragColor = vec4(color, 1);
}