import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class GameWindow {
    Scene scene;
    Stage stage;
    Pane pane;
    double tippx=400, tippy=500, vastasevaljastamisekiirus =20, kuulikiirus =-8, vastasekiirus=0.3;
    int vastane1 =0, kuulid =0, vastane2 =0, triger=0,triger2=0;
    String name;
    Color laevavarv = Color.WHITE, a=Color.CORAL,b=Color.CORAL,c=Color.AQUAMARINE,d=Color.AQUAMARINE;
    Node kshape, ship, vshape, vshape2, vshape3, bar;
    boolean tulistamisealghetk =false, lasevastane=true, kiiruseajamuut =true, barjaarialghetk=false, genbarjaar=false, suunatriger=false, mutesound;
    Kera circle, kuul, barjaar;
    Random juhus;
    AnimationTimer animationTimer;
    int score=0, fullscore=0;
    long vastaseAlgAeg, praegu, kiiruseAlgAeg, barjaariAlgAeg;
    ArrayList<Kera> valang= new ArrayList();
    ArrayList<Kera> vastased= new ArrayList();
    ArrayList<Kera> vastased2= new ArrayList();
    String ssound = "src/sound/ForestSound.mp3";
    MediaPlayer mediaPlayer;
    File file = new File("src/button.css");
    String fileURI = file.toURI().toString();

    public GameWindow(String nimi, boolean mute){
        if (mute) {                                                         //kontrollitakse,kas heli on sees
            try {
                Media sound = new Media(new File(ssound).toURI().toString());
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
            } catch (Exception e){
                System.out.println("Mingi jama playeriga");
            }
        }
        gameWindow();                                                       //mänguakna esile toomine
        spaceShip();                                                        //kosmoslaeva genereerimine ja kustutamine
        startGame();                                                        //animationtimeri käivitamine
        mousePressed();                                                     //kuuli genereerimine
        name=nimi;
        mutesound=mute;
    }
    public void gameWindow(){
        pane = new Pane();                                                  //pane väljatüüp
        pane.setStyle("-fx-background-color: transparent;");
        pane.getStyleClass().add("pane");
        scene= new Scene(pane,800,800);                                     //mõõtudega 800px X 800px
        scene.getStylesheets().add(fileURI);
        stage= new Stage();
        stage.setScene(scene);
        stage.setTitle("Shooter - punktisumma on:  " + fullscore);          //punktisumma kuvamine
        stage.setResizable(false);
        stage.show();                                                       //kuva välja
    }
    public void spaceShip(){                                                //barjääri ja laeva genereerimine hiire liigutamisel
        Kolmnurk laev = new Kolmnurk();                                     //uue Spaceship klassi defineerimine
        scene.setCursor(Cursor.NONE);
        pane.setOnMouseMoved(event -> {                                     //hiire liigutuse peale sündmuse esile kutsumine
            pane.getChildren().removeAll(ship);                             //vana leava eemaldamine
            tippx = event.getSceneX();                                      //hiire x koordinaat
            tippy = event.getSceneY();                                      //hiire y koordinaat
            ship=laev.genKolmnurk(tippx, tippy, laevavarv);                 //laeva seadistamine
            pane.getChildren().add(ship);                                   //laeva kuvamine
            ship.setEffect(new Glow(0.3));
            if (genbarjaar){                                                //barjääri olemasolu kontroll
                try {
                    pane.getChildren().removeAll(bar);                      //barjääri eemaldamine
                } catch (NullPointerException e){                           //püütakse kinni, kui pole barjääri, mida eemmaldada

                }
                genBar();
            }
        });

    }
    public void valjastaVastane(int i, int j){                              //vastase lisamine arraysse
        circle= new Kera();
        Kera circle2= new Kera();
        juhus=new Random();
        int suund;
        if (suunatriger){
            suund=juhus.nextInt(4)+1;
            System.out.println(suund);
            switch (suund){
                case 1:
                    a=Color.CORAL;
                    b=Color.CORAL;
                    c=Color.AQUAMARINE;
                    d=Color.AQUAMARINE;
                    break;
                case 2:
                    a=Color.ORCHID;
                    b=Color.ORCHID;
                    c=Color.DARKGREEN;
                    d=Color.DARKGREEN;
                    break;
                case 3:
                    a=Color.CORAL;
                    b=Color.CORAL;
                    c=Color.DARKGREEN;
                    d=Color.DARKGREEN;
                    break;
                case 4:
                    a=Color.ORCHID;
                    b=Color.CORAL;
                    c=Color.AQUAMARINE;
                    d=Color.DARKGREEN;
                    break;
                default:
                    break;
            }
            suunatriger=false;
        }
        if (triger==0) {
            vshape = circle.kera(juhus.nextInt(600)+100, -50, juhus.nextInt(60) + 20, a, 0);
            if (b==Color.ORCHID)
                triger=0;
            else
                triger=1;
        }
        else {
            vshape = circle.kera(juhus.nextInt(600) - 300, -50, juhus.nextInt(60) + 20, b, 0);
            if (a==Color.CORAL)
                triger=1;
            else
                triger=0;
        }
        vastased.add(i,circle);                                             //vastase lisamine arraysse(kohale i,vastane)
        if (triger2==0) {
            vshape3 = circle2.kera(juhus.nextInt(700) + 500, -50, juhus.nextInt(60) + 20, c, 0);
            if (d==Color.AQUAMARINE)
                triger2=0;
            else
                triger2=1;
        }
        else {
            vshape3 = circle2.kera(900, juhus.nextInt(650)+100, juhus.nextInt(60) + 20, d, 0);
            if (c==Color.DARKGREEN)
                triger2=1;
            else
                triger2=0;
        }
        vastased2.add(j,circle2);
        vshape.setEffect(new Glow(0.8));
        vshape3.setEffect(new Glow(0.8));
        pane.getChildren().addAll(vshape,vshape3);
    }
    public void timeController(){                                           //erinevate liikumissammude muut
        vastasekiirus += 0.3;                                               //vasatse liikumise sammu suurendamine
        System.out.println("Kiirus on "+ vastasekiirus);
        kuulikiirus -= 0.7;                                                 //kuuli kiiruse muut
        System.out.println("Kuuli kiirus on "+ kuulikiirus);
    }
    private void startGame(){                                               //erinevate aegade registreerimine ja objektide liigutamine ajas
        animationTimer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                praegu=now;                                                 //globaalne ajahetk
                if (kiiruseajamuut) {
                    kiiruseAlgAeg = Math.round(now / 1_000_000_000);        //algaeg mängukiiruse muutmiseks
                    kiiruseajamuut =false;
                }
                if (barjaarialghetk) {
                    barjaariAlgAeg = Math.round(now / 1_000_000_000);       //barjääri alghetke salvestamine
                    genbarjaar=true;
                    genBar();
                    barjaarialghetk =false;
                }
                if (Math.round(now/1_000_000_000)-barjaariAlgAeg==10){      //10 sekundi möödudes eemaldatakse barjäär
                    genbarjaar=false;
                    pane.getChildren().remove(bar);
                }

                if (score != 0 && score%5000==0) {                          //iga 5000 punkti tagant muudetakse laeva värvi
                    laevavarv=Color.BLUEVIOLET;
                }
                if (lasevastane){                                           //kui tõene väljastatakse vastane
                    valjastaVastane(vastane1, vastane2);                    //vastane väljastatakse
                    vastaseAlgAeg =Math.round(now / 100_000_000);           //salvestatakse vastase algaeg
                    vastane1++;                                             //suurendatakse juba järgmist vastase salvestamise positsiooni arrays
                    vastane2++;
                    lasevastane=false;                                      //vastase väljastamine lõpetatakse pidevalt jooksvas tsükklis
                }
                if (vastasekiirus <1) {                                                                 //kui vastase samm on
                    if (Math.round(now / 100_000_000) - vastaseAlgAeg == vastasevaljastamisekiirus) {   //kontrollitakse, kas praeguse aja ja algusaja vahe on 1 sekund
                        lasevastane = true;                                                             //tähendab, et iga 1 sekundi järel väljastatkse vastane
                    }else if (vastased.size()==0 || vastased2.size()==0){
                        lasevastane = true;
                    }
                } else if (vastasekiirus >=1 && vastasekiirus <=2){
                    if (Math.round(now / 100_000_000)- vastaseAlgAeg == 15 || Math.round(now / 100_000_000)- vastaseAlgAeg == 14){
                        lasevastane=true;
                    } else if (Math.round(now / 100_000_000) - vastaseAlgAeg == vastasevaljastamisekiirus) {
                        lasevastane = true;
                    } else if (vastased.size()==0 || vastased2.size()==0){
                        lasevastane = true;
                    }

                } else if (vastasekiirus >2){
                   if (Math.round(now / 100_000_000)- vastaseAlgAeg == 10 || Math.round(now / 100_000_000)- vastaseAlgAeg == 9){
                        lasevastane=true;
                    }else if (Math.round(now / 100_000_000) - vastaseAlgAeg == vastasevaljastamisekiirus) {
                        lasevastane = true;
                    }
                   else if (vastased.size()==0 || vastased2.size()==0){
                       lasevastane = true;
                   }
                }
                if (tulistamisealghetk){
                    tulista(kuulid);                                                        //kui tõene kutsutakse esile tulistamine arrays kohal kuulid
                    kuulid++;
                    tulistamisealghetk = false;
                }
                for (int i=0;i<vastased.size();i++) {                                       //käiakse läbi kõik vastased arrays nende liigutamiseks
                    if(vastased.get(i).circle.getFill() == Color.ORCHID){
                        vshape = vastased.get(i).liigutaKuuli(vastasekiirus+1);
                    } else if (vastased.get(i).circle.getFill() == Color.CORAL) {
                        vshape = vastased.get(i).liiguXY(vastasekiirus);                    //kutsutakse välja vastase liikumine vastava kujundi klassis
                    }
                    int arrayidentifier=1;
                    kontrolliBarShip(vshape,i,arrayidentifier);
                }
                for (int i=0;i<vastased2.size();i++) {                                      //käiakse läbi kõik vastased arrays
                     if(vastased2.get(i).circle.getFill() == Color.AQUAMARINE){
                        vshape = vastased2.get(i).negliiguXY(vastasekiirus);
                    } else if (vastased2.get(i).circle.getFill() == Color.DARKGREEN) {
                        vshape = vastased2.get(i).liigumiinusX(vastasekiirus+1);            //kutsutakse välja vastase liikumine vastava kujundi klassis
                    }
                    int arrayidentifier=2;
                    kontrolliBarShip(vshape,i,arrayidentifier);
                }

                if (valang.size() > 0) {                                                    //kui eksisteerib tulistamine
                    for (int j = 0; j < valang.size(); j++) {                               //käiakse läbi kõik kuulid arrays
                        kshape = valang.get(j).liigutaKuuli(kuulikiirus);                   //kuuli liikumise esile kutsumine
                        if (kshape.intersects(0, -20, 800, 5)) {                            //kuuli kontroll kujutletava piiriga ulevalpool nähtavat ekraani
                            valang.remove(j);                                               //sellisel juhul kuuli eemladamine
                            kuulid--;                                                       //array suuruse vähendamine 1 võõra, mida kontrollitakse loobis
                        }
                    }
                    for (int i = 0; i < vastased.size(); i++) {                             //kontrollitakse kuuli kokkupõrget vstase array 1ga
                        vshape = vastased.get(i).circle;
                        int arrayidentifier=1;
                        kontrolliKuuli(vshape, i, arrayidentifier);
                    }
                    for (int i = 0; i < vastased2.size(); i++) {                            //kontrollitakse kuuli kokkupõrget vstase array 2ga
                        vshape = vastased2.get(i).circle;
                        int arrayidentifier=2;
                        kontrolliKuuli(vshape, i, arrayidentifier);
                    }
                }
                if (Math.round(now / 1_000_000_000)- kiiruseAlgAeg == 30) {                 //iga 30 sekundi tagant muudetakse vastase sammu
                    if (vastasekiirus <3) {                                                 //vastase samm ei muutu üle 3 ühiku
                        timeController();                                                   //globaalse sammumuutuse esile kutsumine
                    }
                    kiiruseAlgAeg = Math.round(now / 1_000_000_000);                        //salvestatakse kiiruse muutmiseks vaja minev algaeg, mille suhtes kiirust muudetakse
                    suunatriger= true;
                }
            }
        };
        animationTimer.start();                                                             //käivitatakse ajaliselt jooksev süsteem
    }
    public void mousePressed(){
        pane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {                                 //premkliki defineerimine
                tulistamisealghetk =true;
                String tsound = "src/sound/ForestSound.mp3";
                player(tsound);
            }
            if (event.getButton() == MouseButton.SECONDARY) {                               //vasakkliki defineerimine
                if (laevavarv == Color.BLUEVIOLET) {
                    barjaarialghetk = true;
                    laevavarv = Color.WHITE;

                }
            }
        });
    }
    public void  genBar(){
        barjaar=new Kera();
        bar=barjaar.kera(tippx, tippy, 100, Color.GOLD,15);         //barjäär(xkoord,ykoord,raadius,värvus,välisjoone paksus)
        bar.setEffect(new Glow(1));
        pane.getChildren().add(bar);                                //lisa barjäär
    }
    public void tulista(int b){
        kuul = new Kera();
        kshape = kuul.kera(tippx,tippy,3,Color.ALICEBLUE,0);                            //kuuli genereerimine
        valang.add(b,kuul);
        pane.getChildren().add(kshape);                                                 //kuuli kuvamine
    }
    public void kontrolliKuuli(Node vshape, int i,int arrayidentifier){
        for (int j = 0; j < valang.size(); j++) {                                       //käiakse läbi kõik kuulid arrays
            kshape = valang.get(j).circle;                                              //võetakse kuul arrayst
            if (vshape.getBoundsInLocal().intersects(kshape.getBoundsInLocal()))        //võrreldakse mõõtmete kokkulangevust kohalikus koordinaadistikus
                pulletCollision(vshape, kshape, i,j, arrayidentifier);
        }
    }
    public void kontrolliBarShip(Node vshape,int i, int arrayidentifier){               //barjääri ja laeva kokkupõrge vastasega
        if (vshape.intersects(-2000,900,5000,40)) {
            pane.getChildren().remove(vshape);                                          //eemaldatakse vastane
            removeVastane(i,arrayidentifier);
        }
        try {
            if (ship.getBoundsInLocal().intersects(vshape.getBoundsInLocal()))
                shipCollision(vshape);                                                  //kosmoslaeva kokkupõrke vastasega
            if (genbarjaar)
                if (bar.getBoundsInLocal().intersects(vshape.getBoundsInLocal()))
                    barjaarCollision(vshape, i,arrayidentifier);                        //vastase kokkupõrge barjääriga
        } catch (NullPointerException e) {
        }
        if (arrayidentifier == 2){
            if (vshape.intersects(-100,-100,40,1500)) {                                 //kui vastane2 põrkub nähtamatu elemntiga vasakul ekraani ääres
                pane.getChildren().remove(vshape);                                      //eemaldatakse vastane
                removeVastane(i,arrayidentifier);
            }
        }
    }
    public void removeVastane(int i,int arrayidentifier){                               //vastase eemaldamine korduvkasutuse tõttu
        if (arrayidentifier == 1) {
            vastased.remove(i);
            vastane1--;
        }
        if (arrayidentifier == 2){
            vastased2.remove(i);
            vastane2--;
        }
    }
    public int scoreCounter(int b){
        score += b;                                                                     //loendab punkte  võrra suurendades
        return score;                                                                   //tagastab punktide väärtuse
    }
    public void pulletCollision(Node vshape, Node kshape, int i,int j, int arrayidentifier){                 //kuuli kokkupõrge vastasega
        if (vshape.getBoundsInLocal().getHeight()>70) {                                 //kui vastase kõrgus on suurem kui 70
            Kera uuscircle = new Kera();                                                //tehakse uus vastane ja aesetatakse väiksemana järgnevalt fikseeritud positsioonile
            double xmax = vshape.getBoundsInLocal().getMaxX();
            double xmin = vshape.getBoundsInLocal().getMinX();
            double ymax = vshape.getBoundsInLocal().getMaxY();
            double ymin = vshape.getBoundsInLocal().getMinY();
            int r = (int) vshape.getBoundsInLocal().getHeight();
            pane.getChildren().removeAll(vshape, kshape);
            valang.remove(j);
            if (arrayidentifier == 1) {
                Paint c = vastased.get(i).circle.getFill();
                vastased.set(i, uuscircle);
                if (c == Color.ORCHID) {
                    vshape2 = uuscircle.kera((xmax - xmin) / 2 + xmin, (ymax - ymin) / 2 + ymin, r / 2 - 20, Color.ORCHID, 0);
                    vshape2.setEffect(new Glow(0.8));
                } else {
                    vshape2 = uuscircle.kera((xmax - xmin) / 2 + xmin, (ymax - ymin) / 2 + ymin, r / 2 - 20, Color.CORAL, 0);
                    vshape2.setEffect(new Glow(0.8));
                }
            } else if (arrayidentifier == 2){
                Paint c = vastased2.get(i).circle.getFill();
                vastased2.set(i, uuscircle);
                if (c == Color.AQUAMARINE) {
                    vshape2 = uuscircle.kera((xmax - xmin) / 2 + xmin, (ymax - ymin) / 2 + ymin, r / 2 - 20, Color.AQUAMARINE, 0);
                    vshape2.setEffect(new Glow(0.8));
                } else {
                    vshape2 = uuscircle.kera((xmax - xmin) / 2 + xmin, (ymax - ymin) / 2 + ymin, r / 2 - 20, Color.DARKGREEN, 0);
                    vshape2.setEffect(new Glow(0.8));
                }
            }
            pane.getChildren().add(vshape2);
            fullscore = scoreCounter(100);
            stage.setTitle("Shooter - punktisumma on:  " + fullscore);
            kuulid--;
        } else {
            String psound = "src/sound/ForestSound.mp3";
            player(psound);
            valang.remove(j);
            valang.removeIf(Objects::isNull);
            vastased.removeIf(Objects::isNull);
            vastased2.removeIf(Objects::isNull);
            pane.getChildren().removeAll(vshape, kshape);
            fullscore = scoreCounter(100);
            stage.setTitle("Shooter - punktisumma on:  " + fullscore);
            removeVastane(i,arrayidentifier);
            kuulid--;
        }
    }
    public void shipCollision(Node vshape){                                     //laeva kokkupõrge vastasega
        String csound = "src/sound/ForestSound.mp3";
        player(csound);
        pane.getChildren().removeAll(vshape, ship, kshape);
        stage.close();
        animationTimer.stop();
        SQLiteAndmed baas=new SQLiteAndmed();
        baas.uhenda();
        baas.lisaKasutaja(name, fullscore);
        Login login=new Login(mutesound);
        mediaPlayer.stop();
        login.gameOver();
    }
    public  void barjaarCollision(Node vshape, int i,int arrayidentifier){      //barjääri kokkupõrge vastasega
        pane.getChildren().remove(vshape);
        fullscore = scoreCounter(100);
        stage.setTitle("Punktisumma on:  " + fullscore);
        removeVastane(i,arrayidentifier);
    }
    public  void  player(String heli){                                          //heli
        if (mutesound) {
            try {
                Media sound = new Media(new File(heli).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setVolume(0.1);
                mediaPlayer.play();
            } catch (Exception e) {
                System.out.println("Mingi jama mediaplayeriga");
            }
        }
    }
}
