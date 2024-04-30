Présentation de Domotix :

L'application Domotix est une application permettant de gérée des capteurs de données et de les assignée dans des salles crée au préalable par l'utilisateur, cette application permet en autre de pouvoir allumer des lumières, gérer le chauffage, ouvrir des volets ou encore de contrôler des alarmes, que ce soit alarme incendie ou alors détection d'intrus.
L'application dispose également de modes automatiques permettant de déclencher certaines actions en fonction des données récupérés par les capteurs comme allumer les lumières quand une personne déclenche le capteur de mouvement, ouvrir ou fermer les volets en fonction de la luminosité, déclencher le chauffage quand la température ambiante est trop faible ou bien encore l'envoie de notifications lors de la détection de flamme ou d'intrus.
 
Comment se présente l'application :

L'application se compose de 6 différents volets permettant d'afficher les fonctionnalités suivantes

-Une page d'accueil répertoriant les salles crées dans la maison sous forme de boutons ainsi qu'un bouton ajouter permettant l'ajout d'un nouvelle salle.
![Screenshot_20240430_163126_Domotix](https://github.com/uvsq22101464/projetL3/assets/91185466/a962f757-3094-4c2b-8625-5a53f8b0e024)

-Une page de création de nouvelle salle dans laquelle il est possible de renseigné le nom de la salle voulu, et d'y ajouter les capteurs présents (1 seul exemplaire identique possible).
![Screenshot_20240430_163224_Domotix](https://github.com/uvsq22101464/projetL3/assets/91185466/e7d310bd-32c5-4e12-8b2a-214a5ecd69f0)


-Une page de modification quand on clique sur une des salles de la page d'accueil puis sur modifier qui permet de renommée la salle ou d'y ajouter de nouveaux capteurs.
(Insérer capture d'écran d'une modification)

-Une page de gestion lors du clique sur une salle depuis la page d'accueil. Cette page répertorie les capteurs présent ainsi que les données relatives à ces derniers, depuis cette page il est possible de contrôler les lumières, volets, les modes automatiques ainsi que les seuils servant aux modes automatiques comme la luminosité ou alors la température cible pour le chauffage.
![Screenshot_20240430_163612_Domotix](https://github.com/uvsq22101464/projetL3/assets/91185466/e9057048-e9eb-4f38-97cf-806d0372201b)

-Une page simplifier qui répertorie tous les modes automatiques et permet de les contrôler depuis cette page.
(Insérer capture d'écran de la page mode)

-Une page affichant un graphique avec les températures enregistrées.
![Screenshot_20240430_163144_Domotix](https://github.com/uvsq22101464/projetL3/assets/91185466/7d02ddbf-f0bc-4d36-921c-330a96d29e27)

Fonctionnement détaillés :

L'applicatation à été réalisée avec Android Studio ainsi que java pour gérer tout ce qui est gestion des données et l'affichage sur l'application est en xml, l'application utilise aussi la librairie MPAndroidChart par PhilJay (https://github.com/PhilJay/MPAndroidChart) afin d'afficher un graphique.
Lorsque que l'application se lance, elle se connecte à internet et va récupérer les données situé dans la Realtime Database de firebase pour récupérer en particulier les noms des salles crées et les afficher sur l'écran d'accueil sous forme de bouton cliquable.
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
Dans ce code on va se connectée à la base de données firebase avec "FirebaseDatabase.getInstance("lien de la base)" et la fonction "addOnCompleteListener" va permettre de récupérer les données, pour ce faire les données de firebase vont être converties en un arbre JSON, ensuite on va crée des boutons avec les noms des salles et lors du clique sur l'un d'eux on va récupérer les capteurs, leur valeurs et lancer une nouvelle activitée.
```java
Intent ia = new Intent(MainActivity.this, Manage_room.class);
ia.putExtra("roomCaptor", roomCaptor);
startActivity(ia);
```
cette partie permet de transmettre des valeurs qui on été récupéré depuis la page d'accueil vers la page suivant qui s'ouvrira lors du clique sur le bouton.


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
Ici la fonction est appelé lors d'un changement dans la Realtime Database et en fonction de la valeur qui change va regarder le capteur concerné et afficher à l'utilisateur la modification.
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
Dans ce code la lampe change de valeur et l'assigne dans la base de donnée avec "databaseRef.child("Action/Lampe").setValue(!value);"

A quoi ressemble la création d'un menu :
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
Dans cette section différent layouts sont crée afin de contenir des textes ou alors des graphiques.
