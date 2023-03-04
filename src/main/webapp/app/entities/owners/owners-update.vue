<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="petclinicApp.owners.home.createOrEditLabel" data-cy="OwnersCreateUpdateHeading">Create or edit a Owners</h2>
        <div>
          <div class="form-group" v-if="owners.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="owners.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owners-firstname">Firstname</label>
            <input
              type="text"
              class="form-control"
              name="firstname"
              id="owners-firstname"
              data-cy="firstname"
              :class="{ valid: !$v.owners.firstname.$invalid, invalid: $v.owners.firstname.$invalid }"
              v-model="$v.owners.firstname.$model"
              required
            />
            <div v-if="$v.owners.firstname.$anyDirty && $v.owners.firstname.$invalid">
              <small class="form-text text-danger" v-if="!$v.owners.firstname.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.owners.firstname.maxLength">
                This field cannot be longer than 32 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owners-lastname">Lastname</label>
            <input
              type="text"
              class="form-control"
              name="lastname"
              id="owners-lastname"
              data-cy="lastname"
              :class="{ valid: !$v.owners.lastname.$invalid, invalid: $v.owners.lastname.$invalid }"
              v-model="$v.owners.lastname.$model"
              required
            />
            <div v-if="$v.owners.lastname.$anyDirty && $v.owners.lastname.$invalid">
              <small class="form-text text-danger" v-if="!$v.owners.lastname.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.owners.lastname.maxLength">
                This field cannot be longer than 32 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owners-address">Address</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="owners-address"
              data-cy="address"
              :class="{ valid: !$v.owners.address.$invalid, invalid: $v.owners.address.$invalid }"
              v-model="$v.owners.address.$model"
              required
            />
            <div v-if="$v.owners.address.$anyDirty && $v.owners.address.$invalid">
              <small class="form-text text-danger" v-if="!$v.owners.address.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.owners.address.maxLength">
                This field cannot be longer than 255 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owners-city">City</label>
            <input
              type="text"
              class="form-control"
              name="city"
              id="owners-city"
              data-cy="city"
              :class="{ valid: !$v.owners.city.$invalid, invalid: $v.owners.city.$invalid }"
              v-model="$v.owners.city.$model"
            />
            <div v-if="$v.owners.city.$anyDirty && $v.owners.city.$invalid">
              <small class="form-text text-danger" v-if="!$v.owners.city.maxLength">
                This field cannot be longer than 32 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owners-telephone">Telephone</label>
            <input
              type="text"
              class="form-control"
              name="telephone"
              id="owners-telephone"
              data-cy="telephone"
              :class="{ valid: !$v.owners.telephone.$invalid, invalid: $v.owners.telephone.$invalid }"
              v-model="$v.owners.telephone.$model"
              required
            />
            <div v-if="$v.owners.telephone.$anyDirty && $v.owners.telephone.$invalid">
              <small class="form-text text-danger" v-if="!$v.owners.telephone.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.owners.telephone.maxLength">
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.owners.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./owners-update.component.ts"></script>
