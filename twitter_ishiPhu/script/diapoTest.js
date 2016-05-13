var k_mur={

    tbmage:'',
    nombre:8,
    nb_hauteur:6,
    angle_max:Math.PI/3,
    marge:4,
    chemin:'',
    
    rayon:0,
    hauteur_rectangle:0,
    largeur_rectangle:0,
    crc:0,
    ctn:null,
    sous_ctn:null,
    
    
    init_image:function(){

        this.mamage=new Image
        this.mamage.src=this.chemin+this.tbmage[0]
        this.controle(this);
    },
    
    controle:function(lui){
        
        if(lui.mamage.height!=0){
            lui.azerty();
            return false;
        }
        setTimeout(lui.controle,100,this)
    },

    azerty:function(sens){
    
        var largeur_ctn=this.ctn.offsetWidth/2
        
        this.rayon=largeur_ctn/(Math.sin(this.angle_max))
        
        this.largeur_rectangle=(this.angle_max * this.rayon-this.nombre * (this.marge/2)) / this.nombre*2
        
        this.hauteur_rectangle=(this.mamage.height/this.mamage.width)*this.largeur_rectangle
        
        this.ctn.style.height=this.hauteur_rectangle*this.nb_hauteur+'px'
        
        var sous_ctn=document.createElement('div');
        sous_ctn.className='rotation';
        this.sous_ctn=this.ctn.appendChild(sous_ctn)
        
        this.sous_ctn.style.marginLeft=this.ctn.offsetWidth/2-this.largeur_rectangle/2+'px'
        
        var qt_pi=((this.angle_max) / (this.nombre/2));

        var angle=0;
        var translation_x=0;
        var profondeur_r=0;
        var hauteur=0;
        
        for (var h =0;h<this.nb_hauteur; h++){

            var el_ctr=document.createElement('img');

            el_ctr.style.height=this.hauteur_rectangle+'px';
            el_ctr.style.width=this.largeur_rectangle+'px';
            el_ctr.style.position='absolute';
            el_ctr.setAttribute("data-repere",this.crc)

            el_ctr.src=this.chemin+this.tbmage[this.crc];
            this.crc+=1;

            this.sous_ctn.appendChild(el_ctr);
            el_ctr.style.transform='perspective(2500px)translateX(0px)translateZ('+(-this.rayon)+'px)rotateY(0deg)translateY('+(hauteur)+'px)';
            
             new Diaporama(el_ctr,this.tbmage,'img/',1000);

            for (var i =1; i< (this.nombre/2)+1; i++){

                var el_d=document.createElement('img');

                el_d.style.height=this.hauteur_rectangle+'px';
                el_d.style.width=this.largeur_rectangle+'px';
                el_d.style.position='absolute';
                el_d.setAttribute("data-repere",this.crc)
                
                el_d.src=this.chemin+this.tbmage[this.crc];
                this.crc+=1;

                var el_g=document.createElement('img');

                el_g.style.height=this.hauteur_rectangle+'px';
                el_g.style.width=this.largeur_rectangle+'px';
                el_g.style.position='absolute';
                el_g.setAttribute("data-repere",this.crc)

                el_g.src=this.chemin+this.tbmage[this.crc];
                this.crc+=1;
        
                angle+=qt_pi;

                profondeur_r=Math.round(Math.cos(angle)*this.rayon);

                translation_x=Math.round((Math.sin(angle))*this.rayon);

                var angle_degre=Math.round(180 * (angle) / Math.PI);

                el_d.style.transform='perspective(2500px)translateX('+translation_x+'px)translateZ('+(-profondeur_r)+'px)rotateY('+(-angle_degre)+'deg)translateY('+(hauteur)+'px)';

                el_g.style.transform='perspective(2500px)translateX('+-translation_x+'px)translateZ('+(-profondeur_r)+'px)rotateY('+angle_degre+'deg)translateY('+(hauteur)+'px)';

                this.sous_ctn.appendChild(el_d);
                this.sous_ctn.appendChild(el_g);
                
                 new Diaporama(el_d,this.tbmage,'img/',1000);
                 new Diaporama(el_g,this.tbmage,'img/',1000);
            }

            angle=0;
            hauteur+=this.hauteur_rectangle+this.marge;
        }

    },

}


function Diaporama( cible,tableau,repertoire,duree){

 this.cible=cible;
 this.Tableau=tableau;

 this.repertoir_image=repertoire;
 
 this.temp=Math.floor(Math.random() * (1500 - 800 + 1) + 800);
 
 var rand=Math.random()
 rand=Math.floor(rand*(tableau.length-1))
 
 this.tbmage=rand;

 this.diap();
}

Diaporama.prototype.diap = function(){

 this.tbmage++;
 this.cible.src=this.repertoir_image+this.Tableau[this.tbmage];
 if(this.tbmage==this.Tableau.length-1){
  this.tbmage=-1;
 }
 var that=this;
 setTimeout( function() { that.diap() }, this.temp );
}