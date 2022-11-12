#version 400 core

in vec2 pass_textureCoords;

out vec4 outColor;

uniform sampler2D textureSampler;
void main() {
    if(texture(textureSampler, pass_textureCoords).a > 0.5f){
        outColor = vec4(1.0, 1.0, 1.0, 1.0);
    }else{
        discard;
    }
}