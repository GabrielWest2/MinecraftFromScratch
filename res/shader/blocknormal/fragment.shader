#version 400 core

in vec2 pass_textureCoords;
in float pass_normal;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void){
    vec4 texel = texture(textureSampler, pass_textureCoords);
  if(texel.a < 0.5)
    discard;
    float g = 0;
 if(pass_normal > 5.5){
    g = 0.025;
 }else if(pass_normal > 4.5){
    g = 0.025;
 } else if(pass_normal > 3.5){
    g = 0.05;
 } else if(pass_normal > 2.5){
    g = 0.05;
 } else if(pass_normal > 1.5){
    g = 0.0;
 } else if(pass_normal > 0.5){
    g = 0.075;
 }

  out_Color = texel + vec4(g*1.5, g*1.5, g*1.5, 1.0);
}