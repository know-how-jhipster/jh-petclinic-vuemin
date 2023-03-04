<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pets">
        <h2 class="jh-entity-heading" data-cy="petsDetailsHeading"><span>Pets</span> {{ pets.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ pets.name }}</span>
          </dd>
          <dt>
            <span>Birthdate</span>
          </dt>
          <dd>
            <span>{{ pets.birthdate }}</span>
          </dd>
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <div v-if="pets.type">
              <router-link :to="{ name: 'TypesView', params: { typesId: pets.type.id } }">{{ pets.type.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="pets.owner">
              <router-link :to="{ name: 'OwnersView', params: { ownersId: pets.owner.id } }">{{ pets.owner.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="pets.id" :to="{ name: 'PetsEdit', params: { petsId: pets.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pets-details.component.ts"></script>
