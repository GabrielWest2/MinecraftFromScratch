#version 400 core

in vec4 pass_uiColor;
in vec2 pass_textureCoords;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec4 uiColor;
void main() {

 outColor = texture(textureSampler, pass_textureCoords);
 if(outColor.a > 0.5){
    outColor = uiColor;
 }else{
    discard;
 }
}