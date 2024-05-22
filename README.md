Présentation de Domotix :

L'application Domotix est une application permettant de gérer des capteurs de données et de les assigner dans des salles crée au préalable par l'utilisateur, cette application permet en outre de pouvoir allumer des lumières, gérer le chauffage, ouvrir des volets ou encore de contrôler des alarmes, tel qu'une alarme à incendie ou alors une détection d'intrus.
L'application dispose également de modes automatiques permettant de déclencher certaines actions en fonction des données récupérées par les capteurs comme allumer les lumières quand une personne déclenche le capteur de mouvement, ouvrir ou fermer les volets en fonction de la luminosité, déclencher le chauffage quand la température ambiante est trop faible ou bien encore l'envoie de notifications lors de la détection de flamme ou d'intrus.
 
Comment se présente l'application :

L'application se compose de 6 différents volets permettant d'afficher les fonctionnalités suivantes

-Une page d'accueil répertoriant les salles créées dans la maison sous forme de boutons ainsi qu'un bouton ajouter permettant l'ajout d'une nouvelle salle.

![Screenshot_20240430_163126_Domot](https://github.com/uvsq22101464/projetL3/assets/91185466/50f9dcb2-a132-45a2-9782-ce42e8a96581)

-Une page de création de nouvelle salle dans laquelle il est possible de renseigner le nom de la salle voulu, et d'y ajouter les capteurs présents (1 seul exemplaire identique possible).

![Screenshot_20240430_163224_Domot](https://github.com/uvsq22101464/projetL3/assets/91185466/bd4e4c04-cb30-4566-bd74-11a13c7a676c)


-Une page de modification quand on clique sur une des salles de la page d'accueil puis sur modifier qui permet de renommée la salle ou d'y ajouter de nouveaux capteurs.

-Une page de gestion lors du clic sur une salle depuis la page d'accueil. Cette page répertorie les capteurs présents ainsi que les données relatives à ces derniers, depuis cette page il est possible de contrôler les lumières, volets, les modes automatiques ainsi que les seuils servant aux modes automatiques comme la luminosité ou alors la température cible pour le chauffage.

![Screenshot_20240430_163612_Domot](https://github.com/uvsq22101464/projetL3/assets/91185466/5dab41b0-6dbf-4342-9290-610faa7eb040)
![image_2024-04-30_171803190](https://github.com/uvsq22101464/projetL3/assets/91185466/3d3d8df0-272d-48e8-8706-da94bb076d2c)



-Une page simplifiée qui répertorie tous les modes automatiques et permet de les contrôler depuis cette page.

![image_2024-04-30_164632300](https://github.com/uvsq22101464/projetL3/assets/91185466/87f252d9-9414-4813-9366-9bd75c46280c)


-Une page affichant un graphique avec les températures enregistrées.

![Screenshot_20240430_163144_Domot](https://github.com/uvsq22101464/projetL3/assets/91185466/668a0e10-3b97-4bfa-9cf4-de22158af9e7)

Fonctionnement et interfaces :

La page d'accueil est composée de plusieurs boutons représentant les salles créées au préalable, ces boutons, une fois cliqués envoient vers une page de contrôle permettant de gérer l'activation ou la désactivation des différentes fonctionnalités comme allumer les lumières, fermer les volets... cette page de contrôle permet aussi de visualiser la température ou le taux d'humidité si les capteurs associés y ont été assignés. En cas d'erreur lors de la création de la salle il est possible de la supprimer ou bien alors de la modifier en changeant le nom ou en ajoutant de nouveaux capteurs.
La page d'accueil dispose également d'un bouton permettant d'ajouter une nouvelle salle dans laquelle il est possible d'y renseigner un nom et les capteurs.
L'application dispose également d'une visualisation graphique de la température dans un menu séparer ainsi qu'une page simplifier réunissant les modes automatiques de l'application, rangés par salles, il est donc possible de gérer les modes facilement qui ensuite déclencheront les actions sans avoir besoin d'intervenir.
Afin de modifier les différents seuils servant au bon déroulement des modes automatiques, deux interfaces sont proposés, la première étant une barre permet d'augmenter ou de diminuer la luminosité tandis que l'interface permettant de changer la température du chauffage est un menu déroulant dans lequel le choix de la température est affiché.
Les boutons contrôlant les actions pour allumer les lumières, activer un mode automatique sont des boutons ON/OFF offrant une utilisation simple.


Fonctionnement détaillés :

L'applicatation a été réalisée avec Android Studio ainsi que java pour gérer tout ce qui est gestion des données et l'affichage sur l'application est en XML, l'application utilise aussi la librairie MPAndroidChart par PhilJay (https://github.com/PhilJay/MPAndroidChart) afin d'afficher un graphique.
Lorsque l'application se lance, elle se connecte à internet et va récupérer les données situées dans la Realtime Database de firebase pour récupérer en particulier les noms des salles créées et les afficher sur l'écran d'accueil sous forme de bouton cliquable.
Code associé :
  ```java
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        Log.d("Layout selected", "accueil");
        DatabaseReference database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        ArrayList<String> roomCaptor = new ArrayList<String>() {};
        ArrayList<Object> roomCaptorData = new ArrayList<Object>() {};
        String[] dataType = {"Action", "Détection", "Mode"};
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                        try {
                            Log.e("JSON data", String.valueOf(task.getResult()));
                            JSONObject data = new JSONObject(convert(task.getResult().getValue()));
                                try {
                                    JSONObject roomData = data.getJSONObject("Maison");
                                    Iterator<String> roomNames = roomData.keys();
                                    while(roomNames.hasNext()) {
                                        String name = roomNames.next();
                                        listRoomNames.add(name);
                                        Log.d("firebase", name);
                                        // on crée le bouton et on lui donne ce qu'il contient
                                        Button button = new Button(context);
                                        button.setText(name);
                                        // on defini le clique du bouton
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent ia = new Intent(MainActivity.this, Manage_room.class);
                                                    // on récup les données de la salle dans un JSONObject
                                                    for (String type : dataType) {
                                                        try {
                                                            JSONObject inRoomData = data.getJSONObject("Maison").getJSONObject(name).getJSONObject(type);
                                                            // on itère sur tous les capteurs présent dans la salle
                                                            Iterator<String> inRoomCaptor = inRoomData.keys();
                                                            ia.putExtra("name", name);
                                                            while (inRoomCaptor.hasNext()) {
                                                                String name = inRoomCaptor.next();
                                                                roomCaptor.add(name);
                                                                roomCaptorData.add(inRoomData.get(name));
                                                            }
                                                            Log.d("DATA ROOM CAPTOR", String.valueOf(roomCaptor));
                                                            ia.putExtra("roomCaptor", roomCaptor);
                                                            ia.putExtra("roomCaptorData", (Serializable) roomCaptorData);
                                                            Log.d("Value sent to manage", String.valueOf(roomCaptorData));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                startActivity(ia);
                                            }
                                        });
                                    }
                                } catch (JSONException ignored) {}
                        }catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            }
        });
        }
```
Dans ce code on va se connecter à la base de données firebase avec "FirebaseDatabase.getInstance("lien de la base).getReference()" et la fonction "addOnCompleteListener" va permettre de récupérer les données, pour ce faire les données de firebase vont être converties en un arbre JSON répertoriant les différentes salles, leur noms ainsi que les capteurs et leur valeurs, ensuite on va créér des boutons avec les noms des salles et lors du clic sur l'un d'eux on va récupérer les capteurs, leurs valeurs et lancer une nouvelle activité.
```java
Intent ia = new Intent(MainActivity.this, Manage_room.class);
ia.putExtra("roomCaptor", roomCaptor);
startActivity(ia);
```
Cette partie permet de transmettre des valeurs qui ont été récupérées depuis la page d'accueil vers la page suivante qui s'ouvrira lors du clic sur le bouton.


La page ainsi lancée comprend une deuxième façon de récupérer les données et elle le fait dès qu'il y a un changement dans la base de données
code associé :
```java
roomCaptor = getIntent().getStringArrayListExtra("roomCaptor");
database = FirebaseDatabase.getInstance("https://projet-l3-maison-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Maison/" + name);
database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String type : dataType) {
                    for (String captor : roomCaptor) {
                        Object value = snapshot.child(type + captor).getValue();
                        if (value != null) {
                            switch (captor) {
                                case "Lampe":
                                    ToggleButton buttonL = findViewById(R.id.lightToggle);
                                    buttonL.setChecked((boolean) value);
                                    break;
                                 // gère les différents cas
```
Ici la fonction est appelé à chaque fois qu'un changement dans la Realtime Database est détecté et en fonction de la valeur qui change on va regarder le capteur concerné et afficher à l'utilisateur la modification.
Les boutons eux, quand ils sont cliqués vont regarder leur valeur dans la base de données avant de la changée, un exemple :
```java
public void onClick(View v) {
    DatabaseReference databaseRef = database.getReference( "Maison/" + name);
    databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        // On change la valeur de la lampe
         @Override
         public void onComplete(@NonNull Task<DataSnapshot> task) {
             if (!task.isSuccessful()) {
                 Log.e("Toggle Button", "error retrieving data", task.getException());
             }
             Object data = task.getResult().child("Action/Lampe").getValue();
             if (data instanceof Boolean) {
                 boolean value = (Boolean) data;
                 databaseRef.child("Action/Lampe").setValue(!value);
             } else {
                 Log.e("Toggle Button", "unexpected value type : " + data.getClass().getSimpleName());
             }
         }
    });
}
```
Dans ce code, on va se placer au niveau de Maison et le nom de la salle concerné pour pouvoir modifier les valeurs, ensuite on instancie la fonction ".addOnCompleteListener()" qui va regarder si le bon type valeur est présent et ainsi modifier la valeur actuel avec "databaseRef.child("Action/Lampe").setValue(!value);" Tout ce bout de code s'éxécute lors du clic sur le bouton.

À quoi ressemble la création d'un menu :
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/ic_launcher_background"
        android:orientation="vertical">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/Blanc_transparent"
            android:gravity="center"
            android:text="@string/temperature"
            android:textColor="@color/Blanc"
            android:textSize="22sp">
        </TextView>
        <com.github.mikephil.charting.charts.LineChart
            android:id="@+id/lineChart"
            android:layout_width="match_parent"
            android:layout_height="500dp"
            android:layout_margin="10dp"
            android:background="#A6000000">
        </com.github.mikephil.charting.charts.LineChart>
    </LinearLayout>
    <include
    layout="@layout/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true">
    </include>
</RelativeLayout>
```
Dans cette section différents layouts sont créés afin de contenir des textes ou alors des graphiques, il est également possible de leur assigner une couleur, une dimension et de les placer.

