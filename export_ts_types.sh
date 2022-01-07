
./gradlew common:data:kspKotlin

cp ./common/data/build/generated/ksp/main/resources/com/nevmem/survey/exported.ts ./web/src/data/exported.ts
