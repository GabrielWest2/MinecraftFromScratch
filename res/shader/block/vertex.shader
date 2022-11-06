#version 400 core

in vec3 position;
in vec2 textureCoords;
in float normal;

out vec2 pass_textureCoords;
out float pass_normal;


uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoords = textureCoords;
    pass_normal = normal;
}