# execute script as sysdba
export ORACLE_SID=nndb
echo -n "Enter sysdba password: "
stty -echo
read password
stty echo
sqlplus -s /nolog<<EOF
connect sys/$password as sysdba;
grant create sequence ,alter any sequence to ankola;
grant create sequence ,alter any sequence to badravathi;
grant create sequence ,alter any sequence to bagalkote;
grant create sequence ,alter any sequence to BELGAUM;
grant create sequence ,alter any sequence to BELLARY;
grant create sequence ,alter any sequence to BHATKAL;
grant create sequence ,alter any sequence to bidar;
grant create sequence ,alter any sequence to BIJAPUR;
grant create sequence ,alter any sequence to bommanahalli;
grant create sequence ,alter any sequence to byatarayanapura;
grant create sequence ,alter any sequence to chamarajanagara;
grant create sequence ,alter any sequence to channapatna;
grant create sequence ,alter any sequence to chikkaballapur;
grant create sequence ,alter any sequence to chikkamagalur;
grant create sequence ,alter any sequence to chintamani;
grant create sequence ,alter any sequence to chitradurga;
grant create sequence ,alter any sequence to dandeli;
grant create sequence ,alter any sequence to DASARAHALLI;
grant create sequence ,alter any sequence to DAVANAGERE;
grant create sequence ,alter any sequence to doddaballapur;
grant create sequence ,alter any sequence to gadag_betageri;
grant create sequence ,alter any sequence to GANGAVATHI;
grant create sequence ,alter any sequence to gokak;
grant create sequence ,alter any sequence to GULBARGA;
grant create sequence ,alter any sequence to harihar;
grant create sequence ,alter any sequence to hassan;
grant create sequence ,alter any sequence to HAVERI;
grant create sequence ,alter any sequence to hospet;
grant create sequence ,alter any sequence to HUBLI_DHARWAD;
grant create sequence ,alter any sequence to karwar;
grant create sequence ,alter any sequence to kengeri;
grant create sequence ,alter any sequence to kolar;
grant create sequence ,alter any sequence to KOPPAL;
grant create sequence ,alter any sequence to KRPURA;
grant create sequence ,alter any sequence to KUNDAPUR;
grant create sequence ,alter any sequence to maddur;
grant create sequence ,alter any sequence to MADIKERE;
grant create sequence ,alter any sequence to mahadevapura;
grant create sequence ,alter any sequence to MANDYA;
grant create sequence ,alter any sequence to MANGALORE;
grant create sequence ,alter any sequence to mysore;
grant create sequence ,alter any sequence to nippani;
grant create sequence ,alter any sequence to puttur;
grant create sequence ,alter any sequence to rabertsonpet;
grant create sequence ,alter any sequence to rabkavi_banahatti;
grant create sequence ,alter any sequence to RAICHUR;
grant create sequence ,alter any sequence to ramangaracity;
grant create sequence ,alter any sequence to RANEBENNUR;
grant create sequence ,alter any sequence to rrnagar;
grant create sequence ,alter any sequence to shahabad;
grant create sequence ,alter any sequence to SHIMOGA;
grant create sequence ,alter any sequence to SIRSI;
grant create sequence ,alter any sequence to SORABA;
grant create sequence ,alter any sequence to TUMKUR;
grant create sequence ,alter any sequence to ullal;
grant create sequence ,alter any sequence to udupi;
grant create sequence ,alter any sequence to yelahanka;
grant create sequence ,alter any sequence to ilkal;
grant create sequence ,alter any sequence to yadgir;
grant create sequence ,alter any sequence to sindhanur;
grant create sequence ,alter any sequence to badami;
grant create sequence ,alter any sequence to basava_kalyan;
grant create sequence ,alter any sequence to jamkhandi;
grant create sequence ,alter any sequence to hosakote;
grant create sequence ,alter any sequence to holenarasipur;
grant create sequence ,alter any sequence to mulbagal;
grant create sequence ,alter any sequence to kanakapura;
grant create sequence ,alter any sequence to malavalli;
grant create sequence ,alter any sequence to gowribidanur;
grant create sequence ,alter any sequence to kadur;
grant create sequence ,alter any sequence to bangarpet;
grant create sequence ,alter any sequence to gundlupet;
grant create sequence ,alter any sequence to test;
grant create sequence ,alter any sequence to alanda;
grant create sequence ,alter any sequence to anekal;
grant create sequence ,alter any sequence to annigeri;
grant create sequence ,alter any sequence to arasikere;
grant create sequence ,alter any sequence to athani;
grant create sequence ,alter any sequence to bailhongala;
grant create sequence ,alter any sequence to bhalki;
grant create sequence ,alter any sequence to birur;
grant create sequence ,alter any sequence to byadagi;
grant create sequence ,alter any sequence to challakere;
grant create sequence ,alter any sequence to channarayapatna;
grant create sequence ,alter any sequence to chikkanayakanahalli;
grant create sequence ,alter any sequence to chikkodi;
grant create sequence ,alter any sequence to chittaguppa;
grant create sequence ,alter any sequence to chittapura;
grant create sequence ,alter any sequence to devanahalli;
grant create sequence ,alter any sequence to gajendragada;
grant create sequence ,alter any sequence to guledagudda;
grant create sequence ,alter any sequence to hanagal;
grant create sequence ,alter any sequence to hiriyur;
grant create sequence ,alter any sequence to humnabad;
grant create sequence ,alter any sequence to hunsur;
grant create sequence ,alter any sequence to indi;
grant create sequence ,alter any sequence to kampli;
grant create sequence ,alter any sequence to karkala;
grant create sequence ,alter any sequence to kollegala;
grant create sequence ,alter any sequence to krnagara;
grant create sequence ,alter any sequence to kumata;
grant create sequence ,alter any sequence to kunigal;
grant create sequence ,alter any sequence to lakshmishwara;
grant create sequence ,alter any sequence to madhugiri;
grant create sequence ,alter any sequence to magadi;
grant create sequence ,alter any sequence to mahalingapura;
grant create sequence ,alter any sequence to malur;
grant create sequence ,alter any sequence to manvi;
grant create sequence ,alter any sequence to moodbidri;
grant create sequence ,alter any sequence to mudalagi;
grant create sequence ,alter any sequence to muddebihal;
grant create sequence ,alter any sequence to mudhol;
grant create sequence ,alter any sequence to nanjanagudu;
grant create sequence ,alter any sequence to naragunda;
grant create sequence ,alter any sequence to ramdurga;
grant create sequence ,alter any sequence to sagara;
grant create sequence ,alter any sequence to sakaleshpura;
grant create sequence ,alter any sequence to sankeshwara;
grant create sequence ,alter any sequence to saundatti;
grant create sequence ,alter any sequence to savanur;
grant create sequence ,alter any sequence to sedam;
grant create sequence ,alter any sequence to shahapura;
grant create sequence ,alter any sequence to shidlaghatta;
grant create sequence ,alter any sequence to shikaripura;
grant create sequence ,alter any sequence to sindagi;
grant create sequence ,alter any sequence to sira;
grant create sequence ,alter any sequence to srirangapatna;
grant create sequence ,alter any sequence to surpur;
grant create sequence ,alter any sequence to talikote;
grant create sequence ,alter any sequence to tarikere;
grant create sequence ,alter any sequence to tiptur;
grant create sequence ,alter any sequence to vijayapura;
grant create sequence ,alter any sequence to wadi;
grant create sequence ,alter any sequence to basavanabagewadi;
grant create sequence ,alter any sequence to hirekerur;
grant create sequence ,alter any sequence to pandavapura;
grant create sequence ,alter any sequence to siddapura;
commit;
exit;
EOF
