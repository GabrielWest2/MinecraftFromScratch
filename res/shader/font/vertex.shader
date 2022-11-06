#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec4 pass_uiColor;
out vec2 pass_textureCoords;

uniform vec2 viewport;
uniform vec4 uiColor;
uniform mat4 rotationMat;

void main() {
    vec2 coord = position.xy / viewport;
    coord.y = (1.0-coord.y);
    coord.xy = coord.xy * 2.0 - 1.0;
    gl_Position = rotationMat * vec4(coord.xy, 0.0, 1.0);
    pass_uiColor = uiColor;
    pass_textureCoords = textureCoords;
}