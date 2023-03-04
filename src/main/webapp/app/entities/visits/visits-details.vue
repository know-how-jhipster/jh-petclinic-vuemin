<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="visits">
        <h2 class="jh-entity-heading" data-cy="visitsDetailsHeading"><span>Visits</span> {{ visits.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Visitdate</span>
          </dt>
          <dd>
            <span>{{ visits.visitdate | formatDate }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ visits.description }}</span>
          </dd>
          <dt>
            <span>Pet</span>
          </dt>
          <dd>
            <div v-if="visits.pet">
              <router-link :to="{ name: 'PetsView', params: { petsId: visits.pet.id } }">{{ visits.pet.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="visits.id" :to="{ name: 'VisitsEdit', params: { visitsId: visits.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./visits-details.component.ts"></script>
