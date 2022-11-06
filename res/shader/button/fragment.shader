#version 400 core

in vec2 pass_textureCoords;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform int hovered;
uniform int disabled;
void main() {
    if(disabled == 1){
        outColor = texture(textureSampler, pass_textureCoords + vec2(0, 0.666666));
    }else{
        if(hovered == 1){
            outColor = texture(textureSampler, pass_textureCoords + vec2(0, 0.333333));
        }else{
            outColor = texture(textureSampler, pass_textureCoords);
        }
    }
}