# Atomic-RayTracer
```
#####################
#*  * **** *  *  ** #
#** * *  * ** * *  *#
#* ** **** * ** *  *#
#*  * *  * *  *  ** #
#####################
```
DevelopedBy: nanoTech e Cica
email: nanotechbr.corp@gmail.com
youtube: https://www.youtube.com/user/LegiaoGamerBR

Raytracer por Thiago Figueiredo Costa e Gustavo Ceconelli

---------------------------------------------------------

Codigo: https://github.com/thiagofigcosta/Atomic-RayTracer

VIDEO: https://youtu.be/n8a8JEEygTE

VIDEO 2: https://www.youtube.com/watch?v=523vDvJLlK8

OBJ LINA(muito grande): http://www.4shared.com/file/ztqicIJSce/Lina.html

Para excecutar no windows/linux/qualquer sistema com java:
	1-arrastar o .jar da pasta bin para a pasta do projeto e executar no terminal: "java -jar Atomic-RayTracer.jar cena-lina.txt Lina" os dois ultimos argumentos fazem referencia respectivamente a cena que será usada e a imagem de saida.
	2-abrir o projeto usando o netbeans, argumentos para todas as cenas se encontram no projeto.

Modificações:
	-resolução da imagem de saida aumentada
	-reorganização de arquivos, cenas e objs dentro de "res/" e imagens de saida dentro de "output/
	-criação de função para reflexão de vetores com base na normal em Vector3
	-possibilidade de renderizar arquivos obj, usando a ideia de muitos vertices em forma de esfera com um raio muito pequeno(como uma molecula ou atomo), é possivel alterar a posição, rotação e escala dos objs. Exemplo cena-obj.txt tem um exemplo simples, cena-lina.txt possui um exemplo mais complexo(como o arquivo é muito grande o computador demora uma eternidade+1 para carregar/renderizar o arquivo), mas apresenta um bom funcionamento até onde o processamento conseguiu mostrar, em uma tentativa final deixarei o computador rodando o programa por tempo indeterminado para ver se chegamos a um resultado final.(há uma variavel que computa a quantidade vertices maximos que podem ser carregados)
	-possibilidade de disparar n raios de cada pixel para gerar efeitos de foco, mais evidente nas versões sem iluminação(disponivel em branchs anteriores do git), para fins de otimização apenas um raio esta sendo lançado.(não foi possivel trabalhar mais nessa função devido ao tempo)
	-materiais reflexivos e transparentes (não finalizado devido ao prazo)																																																																																																																																		"
