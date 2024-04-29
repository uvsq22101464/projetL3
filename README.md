Présentation de Domotix :

L'application Domotix est une application permettant de gérée des capteurs de données et de les assignée dans des salles crée au préalable par l'utilisateur, cette application permet en autre de pouvoir allumer des lumières, gérer le chauffage, ouvrir des volets ou encore de contrôler des alarmes, que ce soit alarme incendie ou alors détection d'intrus.
L'application dispose également de modes automatiques permettant de déclencher certaines actions en fonction des données récupérés par les capteurs comme allumer les lumières quand une personne déclenche le capteur de mouvement, ouvrir ou fermer les volets en fonction de la luminosité, déclencher le chauffage quand la température ambiante est trop faible ou bien encore l'envoie de notifications lors de la détection de flamme ou d'intrus.
 
Comment se présente l'application :

L'application se compose de 6 différents volets permettant d'afficher les fonctionnalités suivantes

-Une page d'accueil répertoriant les salles crées dans la maison sous forme de boutons ainsi qu'un bouton ajouter permettant l'ajout d'un nouvelle salle.
(Insérer capture d'écran accueil)

-Une page de création de nouvelle salle dans laquelle il est possible de renseigné le nom de la salle voulu, et d'y ajouter les capteurs présents (1 seul exemplaire identique possible).
(Insérer capture d'écran d'une création)

-Une page de modification quand on clique sur une des salles de la page d'accueil puis sur modifier qui permet de renommée la salle ou d'y ajouter de nouveaux capteurs.
(Insérer capture d'écran d'une modification)

-Une page de gestion lors du clique sur une salle depuis la page d'accueil. Cette page répertorie les capteurs présent ainsi que les données relatives à ces derniers, depuis cette page il est possible de contrôler les lumières, volets, les modes automatiques ainsi que les seuils servant aux modes automatiques comme la luminosité ou alors la température cible pour le chauffage.
(Insérer capture d'écran de plusieurs gestions)

-Une page simplifier qui répertorie tous les modes automatiques et permet de les contrôler depuis cette page.
(Insérer capture d'écran de la page mode)

-Une page affichant un graphique avec les températures enregistrées.
(Insérer capture d'écran du graphique des températures)

Fonctionnement détaillés :

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
                                        // on crée le textView et le bouton et on leur donne ce qu'ils contiennent
                                        Button button = new Button(context);
                                        button.setText(name);
                                        button.setGravity(Gravity.CENTER);
                                        // on def le click du bouton
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
                                        TableRow.LayoutParams parameter = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                                        button.setLayoutParams(parameter);
                                        table = (TableRow) findViewById(R.id.table);
                                        table.addView(button);
                                        SharedPreferences storage = getSharedPreferences("data", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor modif_storage = storage.edit();
                                        modif_storage.putString("listRoom", listRoomNames.toString().substring(1, listRoomNames.toString().length() - 1));
                                        modif_storage.apply();
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
