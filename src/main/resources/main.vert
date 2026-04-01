#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aUv;

out vec3 pos;
out vec3 normal;
out vec2 uv;

uniform mat4 mat_model;
uniform mat3 mat_normal;

uniform mat4 mat_view;
uniform mat4 mat_projection;


void main()
{

    vec4 world_pos = mat_model * vec4(aPos, 1);
    normal = normalize(mat_normal * aNormal);
    gl_Position = mat_projection * mat_view * world_pos;

    pos = vec3(world_pos);
    uv = aUv;
}